import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { StockService } from '../../services/stock.service';
import { ToastService } from '../../services/toast.service';
import { ProductDto } from '../../models/api.models';

@Component({
  selector: 'app-stocks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stocks.component.html',
  styleUrl: './stocks.component.css',
})
export class StocksComponent implements OnInit {
  products = signal<ProductDto[]>([]);

  showSetModal = signal(false);
  selectedProduct = signal<ProductDto | null>(null);
  newAbsoluteQuantity = 0;

  constructor(
    private productService: ProductService,
    private stockService: StockService,
    private toastService: ToastService,
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (res) => this.products.set(res.data || []),
    });
  }

  adjustStock(productId: number, quantityChange: number): void {
    this.stockService.updateStock(productId, quantityChange).subscribe({
      next: () => {
        const sign = quantityChange > 0 ? '+' : '';
        this.toastService.success(`Stock ${sign}${quantityChange} units updated.`, 'Stock Updated');

        this.loadProducts();
      },
    });
  }

  openSetModal(p: ProductDto): void {
    this.selectedProduct.set(p);
    this.newAbsoluteQuantity = p.stock || 0;
    this.showSetModal.set(true);
  }

  closeSetModal(): void {
    this.showSetModal.set(false);
  }

  saveSetStock(): void {
    const prod = this.selectedProduct();
    if (!prod) return;

    this.stockService.setStock(prod.id, this.newAbsoluteQuantity).subscribe({
      next: () => {
        this.toastService.success(
          `Stock quantity has been updated to ${this.newAbsoluteQuantity}.`,
          'Stock Updated',
        );

        this.loadProducts();
        this.closeSetModal();
      },
    });
  }
}
