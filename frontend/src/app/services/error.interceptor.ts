import { HttpInterceptorFn, HttpErrorResponse, HttpContextToken } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ToastService } from './toast.service';

export const SKIP_ERROR_TOAST = new HttpContextToken<boolean>(() => false);

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const skipToast = req.context.get(SKIP_ERROR_TOAST) || req.headers.has('x-skip-toast');

  let modifiedReq = req;
  if (req.headers.has('x-skip-toast')) {
    modifiedReq = req.clone({ headers: req.headers.delete('x-skip-toast') });
  }

  return next(modifiedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (!skipToast) {
        let errorMsg = 'An error occurred.';

        if (error.error instanceof ErrorEvent) {
          errorMsg = error.error.message;
        } else {
          if (error.error && typeof error.error === 'object' && error.error.message) {
            errorMsg = error.error.message;
          } else if (typeof error.error === 'string' && error.error.trim().length > 0) {
            errorMsg = error.error;
          } else if (error.status === 0) {
            errorMsg = 'Cannot reach the server. Please check your connection.';
          } else if (error.status === 401) {
            errorMsg = 'Session expired or unauthorized access.';
          } else if (error.status === 403) {
            errorMsg = 'You do not have permission for this action.';
          } else if (error.status === 404) {
            errorMsg = 'Requested resource not found (404).';
          } else if (error.status === 500) {
            errorMsg = 'A server error occurred (500).';
          } else if (error.message) {
            errorMsg = error.message;
          }
        }

        toastService.error(errorMsg, 'Error');
      }

      return throwError(() => error);
    }),
  );
};
