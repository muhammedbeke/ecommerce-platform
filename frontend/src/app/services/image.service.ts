import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse, ImageDto } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private readonly baseUrl = `${environment.apiUrl}/images`;

  constructor(private http: HttpClient) {}

  uploadImages(productId: number, files: File[]): Observable<ApiResponse<ImageDto[]>> {
    const formData = new FormData();
    formData.append('productId', productId.toString());
    files.forEach((file) => formData.append('files', file));

    return this.http.post<ApiResponse<ImageDto[]>>(`${this.baseUrl}/upload`, formData);
  }

  updateImage(imageId: number, file: File): Observable<ApiResponse<null>> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.put<ApiResponse<null>>(`${this.baseUrl}/image/${imageId}/update`, formData);
  }

  deleteImage(imageId: number): Observable<ApiResponse<null>> {
    return this.http.delete<ApiResponse<null>>(`${this.baseUrl}/image/${imageId}/delete`);
  }

  getImageDownloadUrl(imageId: number): string {
    return `${this.baseUrl}/image/download/${imageId}`;
  }
}
