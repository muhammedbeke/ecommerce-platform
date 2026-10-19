package com.emin.e_commerce.service;

import com.emin.e_commerce.model.BlacklistedToken;
import com.emin.e_commerce.repository.BlacklistedTokenRepository;
import com.emin.e_commerce.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtUtils jwtUtils;

    public void blacklistToken(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        if (blacklistedTokenRepository.existsByToken(token)) {
            return;
        }

        LocalDateTime expiryDate;
        try {
            Date date = jwtUtils.getExpirationDateFromJwtToken(token);
            expiryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            expiryDate = LocalDateTime.now().plusDays(1);
        }

        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return blacklistedTokenRepository.existsByToken(token);
    }
}
