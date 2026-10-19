import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddressDto, AddressRequest, ApiResponse } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AddressService {
  private readonly baseUrl = `${environment.apiUrl}/addresses`;

  constructor(private http: HttpClient) { }

  getMyAddresses(): Observable<ApiResponse<AddressDto[]>> {
    return this.http.get<ApiResponse<AddressDto[]>>(`${this.baseUrl}/my-addresses`, {
      headers: { 'x-skip-toast': 'true' },
    });
  }

  getByCustomerId(customerId: number): Observable<ApiResponse<AddressDto[]>> {
    return this.http.get<ApiResponse<AddressDto[]>>(`${this.baseUrl}/customer/${customerId}`, {
      headers: { 'x-skip-toast': 'true' },
    });
  }

  getById(id: number): Observable<ApiResponse<AddressDto>> {
    return this.http.get<ApiResponse<AddressDto>>(`${this.baseUrl}/address/${id}`);
  }

  create(req: AddressRequest): Observable<ApiResponse<AddressDto>> {
    return this.http.post<ApiResponse<AddressDto>>(`${this.baseUrl}/add`, req);
  }

  update(id: number, req: AddressRequest): Observable<ApiResponse<AddressDto>> {
    return this.http.put<ApiResponse<AddressDto>>(`${this.baseUrl}/address/${id}/update`, req);
  }

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/address/${id}/delete`);
  }
}
