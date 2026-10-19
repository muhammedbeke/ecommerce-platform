import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, BuyingDto, BuyingRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BuyingService {
  private readonly baseUrl = `${environment.apiUrl}/buyings`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<BuyingDto[]>> {
    return this.http.get<ApiResponse<BuyingDto[]>>(`${this.baseUrl}/all`);
  }

  getById(id: number): Observable<ApiResponse<BuyingDto>> {
    return this.http.get<ApiResponse<BuyingDto>>(`${this.baseUrl}/buying/${id}`);
  }

  create(req: BuyingRequest): Observable<ApiResponse<BuyingDto>> {
    return this.http.post<ApiResponse<BuyingDto>>(`${this.baseUrl}/add`, req);
  }

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/buying/${id}/delete`);
  }
}
