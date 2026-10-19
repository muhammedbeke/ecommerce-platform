import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, StockDto } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  private readonly baseUrl = `${environment.apiUrl}/stocks`;

  constructor(private http: HttpClient) {}

  getByProductId(productId: number): Observable<ApiResponse<StockDto>> {
    return this.http.get<ApiResponse<StockDto>>(`${this.baseUrl}/product/${productId}`);
  }

  updateStock(productId: number, quantityChange: number): Observable<ApiResponse<StockDto>> {
    const params = new HttpParams().set('quantityChange', quantityChange.toString());
    return this.http.post<ApiResponse<StockDto>>(`${this.baseUrl}/product/${productId}/update`, null, { params });
  }

  setStock(productId: number, quantity: number): Observable<ApiResponse<StockDto>> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<ApiResponse<StockDto>>(`${this.baseUrl}/product/${productId}/set`, null, { params });
  }
}
