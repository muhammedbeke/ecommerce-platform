import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmService } from '../../services/confirm.service';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.css'
})
export class ConfirmModalComponent {
  constructor(public confirmService: ConfirmService) {}

  onConfirm(): void {
    this.confirmService.handleConfirm();
  }

  onCancel(): void {
    this.confirmService.handleCancel();
  }
}
