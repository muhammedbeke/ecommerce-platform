import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { ImageService } from '../../services/image.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { ProductDto, CategoryDto, ProductRequest } from '../../models/api.models';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
})
export class ProductsComponent implements OnInit {
  products = signal<ProductDto[]>([]);
  categories = signal<CategoryDto[]>([]);

  searchQuery = '';
  selectedCategoryFilter: number | null = null;

  showModal = signal(false);
  editingProduct = signal<ProductDto | null>(null);
  formData: ProductRequest = {
    name: '',
    description: '',
    brand: '',
    categoryId: undefined,
    price: 0,
    lowStockThreshold: 10,
  };

  showImageModal = signal(false);
  selectedProductForImages = signal<ProductDto | null>(null);
  selectedFiles: File[] = [];

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private imageService: ImageService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (res) => this.products.set(res.data || []),
    });
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (res) => this.categories.set(res.data || []),
    });
  }

  selectableCategories(): CategoryDto[] {
    const parentIds = new Set(
      this.categories()
        .map((c) => c.parentCategoryId)
        .filter((id): id is number => id != null && id !== undefined),
    );
    return this.categories().filter((c) => {
      const isParent = !c.parentCategoryId || parentIds.has(c.id);
      return !isParent;
    });
  }

  filteredProducts(): ProductDto[] {
    return this.products().filter((p) => {
      const matchesSearch =
        !this.searchQuery ||
        p.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        (p.brand && p.brand.toLowerCase().includes(this.searchQuery.toLowerCase()));

      const matchesCat =
        this.selectedCategoryFilter === null ||
        this.selectedCategoryFilter === undefined ||
        (this.selectedCategoryFilter as any) === 'null' ||
        (p.category && p.category.id === Number(this.selectedCategoryFilter));

      return matchesSearch && matchesCat;
    });
  }

  openCreateModal(): void {
    this.editingProduct.set(null);
    this.formData = {
      name: '',
      description: '',
      brand: '',
      categoryId: undefined,
      price: 0,
      lowStockThreshold: 10,
    };
    this.showModal.set(true);
  }

  openEditModal(p: ProductDto): void {
    this.editingProduct.set(p);
    this.formData = {
      name: p.name,
      description: p.description,
      brand: p.brand,
      categoryId: p.category?.id,
      price: p.price,
      lowStockThreshold: p.lowStockThreshold ?? 10,
    };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveProduct(): void {
    if (!this.formData.name || this.formData.price === undefined) return;

    if (this.editingProduct()) {
      this.productService.update(this.editingProduct()!.id, this.formData).subscribe({
        next: () => {
          this.toastService.success('Product has been successfully updated.', 'Product Updated');
          this.loadProducts();
          this.closeModal();
        },
      });
    } else {
      this.productService.create(this.formData).subscribe({
        next: () => {
          this.toastService.success('New product has been successfully added.', 'Product Added');
          this.loadProducts();
          this.closeModal();
        },
      });
    }
  }

  async deleteProduct(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Product',
      message: 'Are you sure you want to delete this product?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.productService.delete(id).subscribe({
        next: () => {
          this.toastService.success('Product has been deleted.', 'Product Deleted');
          this.loadProducts();
        },
      });
    }
  }

  openImageModal(p: ProductDto): void {
    this.selectedProductForImages.set(p);
    this.selectedFiles = [];
    this.showImageModal.set(true);
  }

  closeImageModal(): void {
    this.showImageModal.set(false);
  }

  onFileSelected(event: any): void {
    if (event.target.files) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  uploadImages(): void {
    const prod = this.selectedProductForImages();
    if (!prod || this.selectedFiles.length === 0) return;

    this.imageService.uploadImages(prod.id, this.selectedFiles).subscribe({
      next: () => {
        this.toastService.success('Image(s) have been successfully uploaded.', 'Image Uploaded');
        this.productService.getById(prod.id).subscribe({
          next: (res) => {
            this.selectedProductForImages.set(res.data);
            this.loadProducts();
          },
        });
        this.selectedFiles = [];
      },
    });
  }

  deleteImage(imageId: number): void {
    const prod = this.selectedProductForImages();
    if (!prod) return;

    this.imageService.deleteImage(imageId).subscribe({
      next: () => {
        this.toastService.info('Image has been removed.', 'Image Deleted');
        this.productService.getById(prod.id).subscribe({
          next: (res) => {
            this.selectedProductForImages.set(res.data);
            this.loadProducts();
          },
        });
      },
    });
  }

  getImageUrl(imageId: number): string {
    return this.imageService.getImageDownloadUrl(imageId);
  }
}
