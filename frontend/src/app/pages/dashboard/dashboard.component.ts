import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { CustomerService } from '../../services/customer.service';
import { CategoryService } from '../../services/category.service';
import { ProductDto, OrderDto, CustomerDto, CategoryDto } from '../../models/api.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  products = signal<ProductDto[]>([]);
  orders = signal<OrderDto[]>([]);
  customers = signal<CustomerDto[]>([]);
  categories = signal<CategoryDto[]>([]);

  totalProducts = computed(() => this.products().length);
  totalOrders = computed(() => this.orders().length);
  totalCustomers = computed(() => this.customers().length);
  totalCategories = computed(() => this.categories().length);

  pendingOrdersCount = computed(
    () =>
      this.orders().filter((o) => {
        const status = o.orderStatus?.trim().toUpperCase();
        return status === 'PENDING' || status === 'PROCESSING';
      }).length,
  );

  totalRevenue = computed(() =>
    this.orders().reduce((acc, order) => acc + this.calculateOrderTotal(order), 0),
  );

  recentOrders = computed(() => this.orders().slice(0, 5));

  lowStockProducts = computed(() =>
    this.products()
      .filter((p) => p.stock < (p.lowStockThreshold ?? 10))
      .slice(0, 5),
  );

  constructor(
    private productService: ProductService,
    private orderService: OrderService,
    private customerService: CustomerService,
    private categoryService: CategoryService,
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.productService.getAll().subscribe({
      next: (res) => this.products.set(res.data || []),
    });

    this.orderService.getAllOrders().subscribe({
      next: (orders) => this.orders.set(orders || []),
    });

    this.customerService.getAll().subscribe({
      next: (res) => this.customers.set(res.data || []),
    });

    this.categoryService.getAll().subscribe({
      next: (res) => this.categories.set(res.data || []),
    });
  }

  calculateOrderTotal(order: OrderDto): number {
    if (!order.orderDetails) return 0;
    return order.orderDetails.reduce((sum, d) => sum + d.price * d.quantity, 0);
  }

  getStatusBadgeClass(status?: string): string {
    switch (status?.trim().toUpperCase()) {
      case 'PENDING':
        return 'badge bg-warning text-dark';
      case 'PROCESSING':
        return 'badge bg-info text-dark';
      case 'SHIPPED':
        return 'badge bg-primary';
      case 'DELIVERED':
        return 'badge bg-success';
      case 'CANCELLED':
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }
}
