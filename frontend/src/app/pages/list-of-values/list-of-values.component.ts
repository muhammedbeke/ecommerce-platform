import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LovService } from '../../services/lov.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { ListOfValuesDto, ListOfValuesRequest } from '../../models/api.models';

@Component({
  selector: 'app-list-of-values',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list-of-values.component.html',
  styleUrl: './list-of-values.component.css',
})
export class ListOfValuesComponent implements OnInit {
  lovList = signal<ListOfValuesDto[]>([]);
  availableTypes = signal<string[]>([]);
  selectedTypeFilter = signal<string>('ALL');

  showModal = signal(false);
  formData: ListOfValuesRequest = { code: '', value: '', type: 'ORDER_STATUS' };

  constructor(
    private lovService: LovService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadLovs();
  }

  loadLovs(): void {
    this.lovService.getAll().subscribe({
      next: (res) => {
        const list = res.data || [];
        this.lovList.set(list);
        const types = Array.from(new Set(list.map((item) => item.type)));
        this.availableTypes.set(types);
      },
    });
  }

  setTypeFilter(type: string): void {
    this.selectedTypeFilter.set(type);
  }

  filteredLovList(): ListOfValuesDto[] {
    const filter = this.selectedTypeFilter();
    if (filter === 'ALL') return this.lovList();
    return this.lovList().filter((item) => item.type === filter);
  }

  openCreateModal(): void {
    this.formData = { code: '', value: '', type: 'ORDER_STATUS' };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveLov(): void {
    if (!this.formData.code || !this.formData.value || !this.formData.type) return;

    this.lovService.create(this.formData).subscribe({
      next: () => {
        this.toastService.success('LOV Input Created.', 'LOV Saved');
        this.loadLovs();
        this.closeModal();
      },
    });
  }

  async deleteLov(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete LOV Entry',
      message: 'Are you sure you want to delete this list of value entry?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.lovService.delete(id).subscribe({
        next: () => {
          this.toastService.info('LOV Input Deleted.', 'LOV Deleted');
          this.loadLovs();
        },
      });
    }
  }
}
