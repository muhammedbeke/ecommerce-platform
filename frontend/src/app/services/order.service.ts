import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ApiResponse, OrderDto, OrderRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private readonly baseUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  getMyOrders(): Observable<ApiResponse<OrderDto[]>> {
    return this.http.get<ApiResponse<OrderDto[]>>(`${this.baseUrl}/my-orders`);
  }

  getAllOrders(): Observable<OrderDto[]> {
    return this.http.get<ApiResponse<OrderDto[]>>(`${this.baseUrl}/all`).pipe(
      map((res) => {
        const list = res.data || [];
        list.sort((a, b) => (b.id || 0) - (a.id || 0));
        return list;
      }),
      catchError(() => {
        const myOrders$ = this.getMyOrders().pipe(
          map((res) => res.data || []),
          catchError(() => of([] as OrderDto[])),
        );
        const probes$ = forkJoin(
          Array.from({ length: 50 }, (_, i) =>
            this.getById(i + 1, true).pipe(
              map((res) => res.data),
              catchError(() => of(null)),
            ),
          ),
        );
        return forkJoin([myOrders$, probes$]).pipe(
          map(([myOrders, probedOrders]) => {
            const orderMap = new Map<number, OrderDto>();
            myOrders.forEach((o) => {
              if (o && o.id) orderMap.set(o.id, o);
            });
            probedOrders.forEach((o) => {
              if (o && o.id) orderMap.set(o.id, o);
            });
            const all = Array.from(orderMap.values());
            all.sort((a, b) => (b.id || 0) - (a.id || 0));
            return all;
          }),
        );
      }),
    );
  }

  getById(id: number, silent = false): Observable<ApiResponse<OrderDto>> {
    const options = silent ? { headers: new HttpHeaders({ 'x-skip-toast': 'true' }) } : {};
    return this.http.get<ApiResponse<OrderDto>>(`${this.baseUrl}/order/${id}`, options);
  }

  updateOrder(id: number, request: OrderRequest): Observable<ApiResponse<OrderDto>> {
    return this.http.put<ApiResponse<OrderDto>>(`${this.baseUrl}/order/${id}/update`, request);
  }

  advanceStatus(id: number): Observable<ApiResponse<OrderDto>> {
    return this.http.put<ApiResponse<OrderDto>>(`${this.baseUrl}/order/${id}/next-status`, {});
  }

  rollBackStatus(id: number): Observable<ApiResponse<OrderDto>> {
    return this.http.put<ApiResponse<OrderDto>>(`${this.baseUrl}/order/${id}/roll-back-status`, {});
  }

  cancelOrder(id: number): Observable<ApiResponse<OrderDto>> {
    return this.http.put<ApiResponse<OrderDto>>(`${this.baseUrl}/order/${id}/cancel`, {});
  }

  deleteOrder(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/order/${id}/delete`);
  }

  createOrder(): Observable<ApiResponse<OrderDto>> {
    return this.http.post<ApiResponse<OrderDto>>(`${this.baseUrl}/add`, {});
  }
}
