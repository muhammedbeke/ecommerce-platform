import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, ListOfValuesDto, ListOfValuesRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LovService {
  private readonly baseUrl = `${environment.apiUrl}/list-of-values`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<ListOfValuesDto[]>> {
    return this.http.get<ApiResponse<ListOfValuesDto[]>>(`${this.baseUrl}/all`);
  }

  getByType(type: string): Observable<ApiResponse<ListOfValuesDto[]>> {
    return this.http.get<ApiResponse<ListOfValuesDto[]>>(`${this.baseUrl}/type/${type}`);
  }

  create(req: ListOfValuesRequest): Observable<ApiResponse<ListOfValuesDto>> {
    return this.http.post<ApiResponse<ListOfValuesDto>>(`${this.baseUrl}/add`, req);
  }

  delete(id: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/value/${id}/delete`);
  }
}
