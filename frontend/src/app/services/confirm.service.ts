import { Injectable, signal } from '@angular/core';

export interface ConfirmOptions {
  title?: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  btnClass?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConfirmService {
  isOpen = signal<boolean>(false);
  title = signal<string>('Confirm Action');
  message = signal<string>('Are you sure you want to proceed?');
  confirmText = signal<string>('Confirm');
  cancelText = signal<string>('Cancel');
  btnClass = signal<string>('btn-danger');

  private resolveCallback: ((value: boolean) => void) | null = null;

  confirm(options: ConfirmOptions): Promise<boolean> {
    this.title.set(options.title || 'Confirm Action');
    this.message.set(options.message);
    this.confirmText.set(options.confirmText || 'Confirm');
    this.cancelText.set(options.cancelText || 'Cancel');
    this.btnClass.set(options.btnClass || 'btn-danger');
    this.isOpen.set(true);

    return new Promise<boolean>((resolve) => {
      this.resolveCallback = resolve;
    });
  }

  handleConfirm(): void {
    this.isOpen.set(false);
    if (this.resolveCallback) {
      this.resolveCallback(true);
      this.resolveCallback = null;
    }
  }

  handleCancel(): void {
    this.isOpen.set(false);
    if (this.resolveCallback) {
      this.resolveCallback(false);
      this.resolveCallback = null;
    }
  }
}
