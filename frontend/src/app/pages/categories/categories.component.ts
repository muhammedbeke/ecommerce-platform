import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoryService } from '../../services/category.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { CategoryDto, CategoryRequest } from '../../models/api.models';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
})
export class CategoriesComponent implements OnInit {
  allCategories = signal<CategoryDto[]>([]);
  mainCategories = signal<CategoryDto[]>([]);
  subCategories = signal<CategoryDto[]>([]);
  selectedParentId = signal<number | null>(null);

  showModal = signal(false);
  editingCategory = signal<CategoryDto | null>(null);
  formData: CategoryRequest = { name: '', description: '', parentCategoryId: null };

  constructor(
    private categoryService: CategoryService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getAll().subscribe({
      next: (res) => {
        const all = res.data || [];
        this.allCategories.set(all);
        this.mainCategories.set(all.filter((c) => !c.parentCategoryId));

        if (this.selectedParentId()) {
          this.subCategories.set(all.filter((c) => c.parentCategoryId === this.selectedParentId()));
        }
      },
    });
  }

  selectMainCategory(parentId: number): void {
    this.selectedParentId.set(parentId);
    this.subCategories.set(this.allCategories().filter((c) => c.parentCategoryId === parentId));
  }

  selectedParentCategory(): CategoryDto | undefined {
    return this.mainCategories().find((c) => c.id === this.selectedParentId());
  }

  openCreateModal(): void {
    this.editingCategory.set(null);
    this.formData = {
      name: '',
      description: '',
      parentCategoryId: this.selectedParentId() || null,
    };
    this.showModal.set(true);
  }

  openEditModal(cat: CategoryDto): void {
    this.editingCategory.set(cat);
    this.formData = {
      name: cat.name,
      description: cat.description,
      parentCategoryId: cat.parentCategoryId || null,
    };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveCategory(): void {
    if (!this.formData.name) return;

    if (this.editingCategory()) {
      this.categoryService.update(this.editingCategory()!.id, this.formData).subscribe({
        next: () => {
          this.toastService.success('Category Updated Successfuly.', 'Category Updated');
          this.loadCategories();
          this.closeModal();
        },
      });
    } else {
      this.categoryService.create(this.formData).subscribe({
        next: () => {
          this.toastService.success('New Category Created.', 'Category Added');
          this.loadCategories();
          this.closeModal();
        },
      });
    }
  }

  async deleteCategory(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Category',
      message: 'Are you sure you want to delete this category?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.categoryService.delete(id).subscribe({
        next: () => {
          this.toastService.success('Category Deleted.', 'Category Deleted');
          this.loadCategories();
        },
      });
    }
  }
}
