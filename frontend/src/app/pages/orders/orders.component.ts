import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { ProductService } from '../../services/product.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { OrderDto, ProductDto, OrderItemRequest } from '../../models/api.models';

interface EditItemForm {
  productId: number;
  quantity: number;
  price: number;
}

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css',
})
export class OrdersComponent implements OnInit {
  orders = signal<OrderDto[]>([]);
  selectedStatusFilter = signal<string>('ALL');
  selectedOrderForDetail = signal<OrderDto | null>(null);

  selectedOrderForEdit = signal<OrderDto | null>(null);
  editItems = signal<EditItemForm[]>([]);
  availableProducts = signal<ProductDto[]>([]);

  constructor(
    private orderService: OrderService,
    private productService: ProductService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadOrders();
    this.loadProducts();
  }

  loadOrders(): void {
    this.orderService.getAllOrders().subscribe({
      next: (orders) => this.orders.set(orders || []),
    });
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (res) => this.availableProducts.set(res.data || []),
    });
  }

  setStatusFilter(status: string): void {
    this.selectedStatusFilter.set(status);
  }

  filteredOrders(): OrderDto[] {
    const filter = this.selectedStatusFilter();
    if (filter === 'ALL') return this.orders();
    return this.orders().filter(
      (o) => o.orderStatus?.trim().toUpperCase() === filter.toUpperCase(),
    );
  }

  calculateOrderTotal(order: OrderDto): number {
    if (!order.orderDetails) return 0;
    return order.orderDetails.reduce((sum, d) => sum + d.price * d.quantity, 0);
  }

  calculateEditTotal(): number {
    return this.editItems().reduce(
      (sum, item) => sum + (item.price || 0) * (item.quantity || 0),
      0,
    );
  }

  canAdvanceOrCancel(status?: string): boolean {
    const st = status?.trim().toUpperCase();
    return st !== 'DELIVERED' && st !== 'CANCELLED';
  }

  canRollBack(status?: string): boolean {
    const st = status?.trim().toUpperCase();
    return st !== 'PENDING';
  }

  rollBackStatus(orderId: number): void {
    this.orderService.rollBackStatus(orderId).subscribe({
      next: () => {
        this.toastService.success(
          `Order #${orderId} status has been roll backed to the back stage.`,
          'Order Roll Backed',
        );
        this.loadOrders();
      },
    });
  }

  advanceStatus(orderId: number): void {
    this.orderService.advanceStatus(orderId).subscribe({
      next: () => {
        this.toastService.success(
          `Order #${orderId} status has been advanced to the next stage.`,
          'Order Advanced',
        );
        this.loadOrders();
      },
    });
  }

  async cancelOrder(orderId: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Cancel Order',
      message: `Are you sure you want to cancel order #${orderId}?`,
      confirmText: 'Cancel Order',
      btnClass: 'btn-warning',
    });
    if (confirmed) {
      this.orderService.cancelOrder(orderId).subscribe({
        next: () => {
          this.toastService.warning(`Order #${orderId} has been cancelled.`, 'Order Cancelled');
          this.loadOrders();
        },
      });
    }
  }

  async deleteOrder(orderId: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Order',
      message: `Are you sure you want to delete order #${orderId}?`,
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.orderService.deleteOrder(orderId).subscribe({
        next: () => {
          this.toastService.info(`Order #${orderId} has been deleted.`, 'Order Deleted');
          this.loadOrders();
        },
      });
    }
  }

  openDetailDrawer(order: OrderDto): void {
    this.selectedOrderForDetail.set(order);
  }

  closeDetailDrawer(): void {
    this.selectedOrderForDetail.set(null);
  }

  openEditModal(order: OrderDto): void {
    if (this.availableProducts().length === 0) {
      this.loadProducts();
    }

    const items: EditItemForm[] = (order.orderDetails || []).map((detail) => ({
      productId: detail.productId,
      quantity: detail.quantity,
      price: detail.price,
    }));

    this.selectedOrderForEdit.set(order);
    this.editItems.set(items.length > 0 ? items : []);
  }

  closeEditModal(): void {
    this.selectedOrderForEdit.set(null);
    this.editItems.set([]);
  }

  addEditItem(): void {
    const firstProduct = this.availableProducts()[0];
    const newItem: EditItemForm = {
      productId: firstProduct ? firstProduct.id : 0,
      quantity: 1,
      price: firstProduct ? firstProduct.price : 0,
    };
    this.editItems.set([...this.editItems(), newItem]);
  }

  removeEditItem(index: number): void {
    const items = [...this.editItems()];
    items.splice(index, 1);
    this.editItems.set(items);
  }

  onProductChange(index: number, productId: number): void {
    const selectedProd = this.availableProducts().find((p) => p.id === Number(productId));
    const items = [...this.editItems()];
    if (items[index]) {
      items[index].productId = Number(productId);
      if (selectedProd) {
        items[index].price = selectedProd.price;
      }
    }
    this.editItems.set(items);
  }

  saveOrderEdit(): void {
    const order = this.selectedOrderForEdit();
    if (!order) return;

    const items = this.editItems();
    if (items.length === 0) {
      this.toastService.error(
        'Please add at least one line item to the order.',
        'Validation Error',
      );
      return;
    }

    for (const item of items) {
      if (!item.productId || item.productId <= 0) {
        this.toastService.error('Please select a valid product for all items.', 'Validation Error');
        return;
      }
      if (!item.quantity || item.quantity <= 0) {
        this.toastService.error(
          'Quantity must be greater than 0 for all items.',
          'Validation Error',
        );
        return;
      }
    }

    const payloadItems: OrderItemRequest[] = items.map((i) => ({
      productId: Number(i.productId),
      quantity: Number(i.quantity),
      price: Number(i.price),
    }));

    this.orderService.updateOrder(order.id, { items: payloadItems }).subscribe({
      next: () => {
        this.toastService.success(
          `Order #${order.id} updated successfully! Status set to PENDING.`,
          'Order Updated',
        );
        this.closeEditModal();
        this.loadOrders();
      },
    });
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
