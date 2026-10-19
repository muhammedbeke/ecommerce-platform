import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { ApiResponse, JwtResponse, LoginRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly baseUrl = `${environment.apiUrl}/auth`;
  private readonly TOKEN_KEY = 'admin_jwt_token';
  private readonly EMAIL_KEY = 'admin_email';
  private readonly ROLE_KEY = 'admin_role';

  readonly token = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));
  readonly email = signal<string | null>(localStorage.getItem(this.EMAIL_KEY));
  readonly role = signal<string | null>(localStorage.getItem(this.ROLE_KEY));

  readonly isLoggedIn = computed(() => !!this.token());
  readonly isAdmin = computed(() => this.role()?.toUpperCase() === 'ADMIN');

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: LoginRequest): Observable<ApiResponse<JwtResponse>> {
    return this.http.post<ApiResponse<JwtResponse>>(`${this.baseUrl}/login`, credentials).pipe(
      tap((res) => {
        if (res.data) {
          this.setSession(res.data);
        }
      })
    );
  }

  logout(): void {
    const currentToken = this.token();
    if (currentToken) {
      this.http.post(`${this.baseUrl}/logout`, {}).subscribe({
        next: () => this.clearSession(),
        error: () => this.clearSession(),
      });
    } else {
      this.clearSession();
    }
  }

  private setSession(jwtResponse: JwtResponse): void {
    localStorage.setItem(this.TOKEN_KEY, jwtResponse.token);
    localStorage.setItem(this.EMAIL_KEY, jwtResponse.email);
    localStorage.setItem(this.ROLE_KEY, jwtResponse.role);

    this.token.set(jwtResponse.token);
    this.email.set(jwtResponse.email);
    this.role.set(jwtResponse.role);
  }

  private clearSession(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.EMAIL_KEY);
    localStorage.removeItem(this.ROLE_KEY);

    this.token.set(null);
    this.email.set(null);
    this.role.set(null);

    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.token();
  }
}
