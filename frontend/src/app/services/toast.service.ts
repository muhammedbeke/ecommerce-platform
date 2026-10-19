import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface Toast {
  id: string;
  type: ToastType;
  title?: string;
  message: string;
  duration?: number;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  toasts = signal<Toast[]>([]);

  show(message: string, type: ToastType = 'info', title?: string, duration: number = 4000): void {
    const id = Math.random().toString(36).substring(2, 9);
    const toastTitle = title || type.charAt(0).toUpperCase() + type.slice(1);
    const toast: Toast = { id, type, title: toastTitle, message, duration };

    this.toasts.update((current) => [...current, toast]);

    if (duration > 0) {
      setTimeout(() => {
        this.remove(id);
      }, duration);
    }
  }

  success(message: string, title: string = 'Success', duration: number = 4000): void {
    this.show(message, 'success', title, duration);
  }

  error(message: string, title: string = 'Error', duration: number = 5000): void {
    this.show(message, 'error', title, duration);
  }

  warning(message: string, title: string = 'Warning', duration: number = 4000): void {
    this.show(message, 'warning', title, duration);
  }

  info(message: string, title: string = 'Info', duration: number = 4000): void {
    this.show(message, 'info', title, duration);
  }

  remove(id: string): void {
    this.toasts.update((current) => current.filter((t) => t.id !== id));
  }
}
