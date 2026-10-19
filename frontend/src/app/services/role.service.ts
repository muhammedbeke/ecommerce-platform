import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, RoleDto, RoleRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private readonly baseUrl = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<RoleDto[]>> {
    return this.http.get<ApiResponse<RoleDto[]>>(`${this.baseUrl}/all`);
  }

  getById(id: number): Observable<ApiResponse<RoleDto>> {
    return this.http.get<ApiResponse<RoleDto>>(`${this.baseUrl}/role/${id}`);
  }

  create(req: RoleRequest): Observable<ApiResponse<RoleDto>> {
    return this.http.post<ApiResponse<RoleDto>>(`${this.baseUrl}/add`, req);
  }

  update(id: number, req: RoleRequest): Observable<ApiResponse<RoleDto>> {
    return this.http.put<ApiResponse<RoleDto>>(`${this.baseUrl}/role/${id}/update`, req);
  }

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/role/${id}/delete`);
  }
}
