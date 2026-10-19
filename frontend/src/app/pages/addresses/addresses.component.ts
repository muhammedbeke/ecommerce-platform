import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AddressService } from '../../services/address.service';
import { CustomerService } from '../../services/customer.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { AddressDto, CustomerDto, AddressRequest } from '../../models/api.models';

@Component({
  selector: 'app-addresses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './addresses.component.html',
  styleUrl: './addresses.component.css',
})
export class AddressesComponent implements OnInit {
  addresses = signal<AddressDto[]>([]);
  customers = signal<CustomerDto[]>([]);

  showModal = signal(false);
  editingAddress = signal<AddressDto | null>(null);
  formData: AddressRequest = { title: '', addressLine: '', customerId: undefined };

  constructor(
    private addressService: AddressService,
    private customerService: CustomerService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadAddresses();
  }

  loadAddresses(): void {
    this.customerService.getAll().subscribe({
      next: (res) => {
        const custs = res.data || [];
        this.customers.set(custs);
        let all: AddressDto[] = [];
        custs.forEach((c) => {
          if (c.addresses) {
            all = all.concat(c.addresses);
          }
        });
        this.addresses.set(all);
      },
    });
  }

  openCreateModal(): void {
    this.editingAddress.set(null);
    const availableCustomers = this.customers().filter((c) => c.id !== 1);
    const defaultCustomerId = availableCustomers.length > 0 ? availableCustomers[0].id : undefined;
    this.formData = { title: '', addressLine: '', customerId: defaultCustomerId };
    this.showModal.set(true);
  }

  openEditModal(addr: AddressDto): void {
    this.editingAddress.set(addr);
    const availableCustomers = this.customers().filter((c) => c.id !== 1);
    const defaultCustomerId = availableCustomers.length > 0 ? availableCustomers[0].id : undefined;
    this.formData = {
      title: addr.title,
      addressLine: addr.addressLine,
      customerId: addr.customerId ?? defaultCustomerId,
    };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveAddress(): void {
    if (!this.formData.title || !this.formData.addressLine || !this.formData.customerId) return;

    if (this.editingAddress()) {
      this.addressService.update(this.editingAddress()!.id, this.formData).subscribe({
        next: () => {
          this.toastService.success('Adress Updated Successfully.', 'Adress Updated');
          this.loadAddresses();
          this.closeModal();
        },
      });
    } else {
      this.addressService.create(this.formData).subscribe({
        next: () => {
          this.toastService.success('New Address Saved.', 'Adress Added');
          this.loadAddresses();
          this.closeModal();
        },
      });
    }
  }

  async deleteAddress(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Address',
      message: 'Are you sure you want to delete this shipping address?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.addressService.delete(id).subscribe({
        next: () => {
          this.toastService.info('Adress deleted.', 'Adres deleted');
          this.loadAddresses();
        },
      });
    }
  }
}
