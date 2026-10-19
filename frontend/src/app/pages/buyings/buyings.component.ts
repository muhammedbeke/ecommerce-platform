import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BuyingService } from '../../services/buying.service';
import { ProductService } from '../../services/product.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { BuyingDto, ProductDto, BuyingItemRequest } from '../../models/api.models';

@Component({
  selector: 'app-buyings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './buyings.component.html',
  styleUrl: './buyings.component.css',
})
export class BuyingsComponent implements OnInit {
  buyings = signal<BuyingDto[]>([]);
  products = signal<ProductDto[]>([]);

  showModal = signal(false);
  buyingItems: BuyingItemRequest[] = [];

  constructor(
    private buyingService: BuyingService,
    private productService: ProductService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadBuyings();
    this.loadProducts();
  }

  loadBuyings(): void {
    this.buyingService.getAll().subscribe({
      next: (res) => this.buyings.set(res.data || []),
    });
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (res) => this.products.set(res.data || []),
    });
  }

  calculateBuyingTotal(buying: BuyingDto): number {
    if (!buying.buyingDetails) return 0;
    return buying.buyingDetails.reduce((sum, d) => sum + d.price * d.quantity, 0);
  }

  openCreateModal(): void {
    this.buyingItems = [{ productId: this.products()[0]?.id || 0, quantity: 10, price: 50.0 }];
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  addItemRow(): void {
    this.buyingItems.push({ productId: this.products()[0]?.id || 0, quantity: 1, price: 10.0 });
  }

  removeItemRow(index: number): void {
    this.buyingItems.splice(index, 1);
  }

  saveBuying(): void {
    if (this.buyingItems.length === 0) return;

    this.buyingService.create({ items: this.buyingItems }).subscribe({
      next: () => {
        this.toastService.success('New Buying record created.', 'Buying saved');
        this.loadBuyings();
        this.loadProducts();
        this.closeModal();
      },
    });
  }

  async deleteBuying(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Purchase',
      message: 'Are you sure you want to delete this purchase record?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.buyingService.delete(id).subscribe({
        next: () => {
          this.toastService.info('Buying Record deleted.', 'Record Deleted');
          this.loadBuyings();
        },
      });
    }
  }
}
