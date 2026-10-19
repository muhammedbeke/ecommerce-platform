import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, CustomerDto, CustomerRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private readonly baseUrl = `${environment.apiUrl}/customers`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<CustomerDto[]>> {
    return this.http.get<ApiResponse<CustomerDto[]>>(`${this.baseUrl}/all`);
  }

  getMe(): Observable<ApiResponse<CustomerDto>> {
    return this.http.get<ApiResponse<CustomerDto>>(`${this.baseUrl}/customer/me`);
  }

  getById(id: number): Observable<ApiResponse<CustomerDto>> {
    return this.http.get<ApiResponse<CustomerDto>>(`${this.baseUrl}/customer/${id}`);
  }

  create(req: CustomerRequest): Observable<ApiResponse<CustomerDto>> {
    return this.http.post<ApiResponse<CustomerDto>>(`${this.baseUrl}/add`, req);
  }

  update(id: number, req: CustomerRequest): Observable<ApiResponse<CustomerDto>> {
    return this.http.put<ApiResponse<CustomerDto>>(`${this.baseUrl}/customer/${id}/update`, req);
  }

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/customer/${id}/delete`);
  }
}
