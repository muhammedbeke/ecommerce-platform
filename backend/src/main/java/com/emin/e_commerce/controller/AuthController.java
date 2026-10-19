package com.emin.e_commerce.controller;

import com.emin.e_commerce.request.LoginRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.response.JwtResponse;
import com.emin.e_commerce.security.JwtUtils;
import com.emin.e_commerce.security.CustomUserDetails;
import com.emin.e_commerce.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElse("ROLE_CUSTOMER");

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getUsername(), role);
        return ResponseEntity.ok(new ApiResponse("Login successful!", jwtResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(new ApiResponse("Logout successful!", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse("Logout failed: No token provided in Authorization header!", null));
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth == null) {
            headerAuth = request.getHeader("authorization");
        }
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}



