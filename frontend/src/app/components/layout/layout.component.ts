import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ToastComponent } from '../toast/toast.component';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { AuthService } from '../../services/auth.service';

export interface NavItem {
  path: string;
  label: string;
  icon: string;
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, ToastComponent, ConfirmModalComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css',
})
export class LayoutComponent {
  readonly navItems: NavItem[] = [
    { path: '/admin/dashboard', label: 'Dashboard', icon: '📊' },
    { path: '/admin/products', label: 'Products', icon: '📦' },
    { path: '/admin/categories', label: 'Categories', icon: '📁' },
    { path: '/admin/orders', label: 'Orders', icon: '🛒' },
    { path: '/admin/buyings', label: 'Buyings', icon: '🚚' },
    { path: '/admin/stocks', label: 'Stock Control', icon: '📈' },
    { path: '/admin/customers', label: 'Customers', icon: '👥' },
    { path: '/admin/addresses', label: 'Addresses', icon: '📍' },
    { path: '/admin/roles', label: 'Roles', icon: '🔑' },
    { path: '/admin/list-of-values', label: 'List of Values', icon: '⚙️' },
  ];

  constructor(private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
