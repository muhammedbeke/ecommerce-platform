import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { AddressService } from '../../services/address.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { CustomerDto, AddressDto, CustomerRequest, AddressRequest } from '../../models/api.models';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
})
export class CustomersComponent implements OnInit {
  customers = signal<CustomerDto[]>([]);

  showModal = signal(false);
  editingCustomer = signal<CustomerDto | null>(null);
  validationError = signal<string | null>(null);
  formData: CustomerRequest = { email: '', phone: '', password: '' };

  selectedCustomerForAddresses = signal<CustomerDto | null>(null);
  customerAddresses = signal<AddressDto[]>([]);
  newAddress: AddressRequest = { title: '', addressLine: '' };

  constructor(
    private customerService: CustomerService,
    private addressService: AddressService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getAll().subscribe({
      next: (res) => this.customers.set(res.data || []),
    });
  }

  openCreateModal(): void {
    this.editingCustomer.set(null);
    this.validationError.set(null);
    this.formData = { email: '', phone: '', password: '' };
    this.showModal.set(true);
  }

  openEditModal(cust: CustomerDto): void {
    this.editingCustomer.set(cust);
    this.validationError.set(null);
    this.formData = { email: cust.email, phone: cust.phone, password: '' };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveCustomer(): void {
    if (!this.formData.email || !this.formData.phone) {
      const msg = 'Email and phone are required.';
      this.validationError.set(msg);
      this.toastService.warning(msg, 'Lack Information');
      return;
    }

    if (!this.editingCustomer() && !this.formData.password) {
      const msg = 'Password is required for new customer registration.';
      this.validationError.set(msg);
      this.toastService.warning(msg, 'Password necessary');
      return;
    }

    if (this.formData.password) {
      const pwdRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).+$/;
      if (this.formData.password.length < 6 || !pwdRegex.test(this.formData.password)) {
        const msg = 'Password must be 6-20 chars with uppercase, lowercase, and a number.';
        this.validationError.set(msg);
        this.toastService.warning(msg, 'Unvalid Password');
        return;
      }
    }

    if (this.editingCustomer()) {
      this.customerService.update(this.editingCustomer()!.id, this.formData).subscribe({
        next: () => {
          this.toastService.success('Customer Information Updated.', 'Customer Updated');
          this.loadCustomers();
          this.closeModal();
        },
        error: (err) => {
          const msg = err?.error?.message || 'Error updating customer';
          this.validationError.set(msg);
        },
      });
    } else {
      this.customerService.create(this.formData).subscribe({
        next: () => {
          this.toastService.success('New Customer Record Created.', 'Customer Added');
          this.loadCustomers();
          this.closeModal();
        },
        error: (err) => {
          const msg = err?.error?.message || 'Error registering customer';
          this.validationError.set(msg);
        },
      });
    }
  }

  async deleteCustomer(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Customer',
      message: 'Are you sure you want to delete this customer?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.customerService.delete(id).subscribe({
        next: () => {
          this.toastService.success('Customer Deleted.', 'Customer Deleted');
          this.loadCustomers();
        },
      });
    }
  }

  openAddressesDrawer(cust: CustomerDto): void {
    this.selectedCustomerForAddresses.set(cust);
    this.customerAddresses.set([]);
    this.newAddress = { title: '', addressLine: '', customerId: cust.id };
    this.loadCustomerAddresses(cust.id);
  }

  closeAddressesDrawer(): void {
    this.selectedCustomerForAddresses.set(null);
    this.customerAddresses.set([]);
  }

  loadCustomerAddresses(customerId: number): void {
    this.addressService.getByCustomerId(customerId).subscribe({
      next: (res) => this.customerAddresses.set(res.data || []),
      error: () => this.customerAddresses.set([]),
    });
  }

  addAddressToCustomer(): void {
    const cust = this.selectedCustomerForAddresses();
    if (!cust || !this.newAddress.title || !this.newAddress.addressLine) {
      this.toastService.warning('Fill Adress Title and Detail.', 'Lack Information');
      return;
    }

    this.newAddress.customerId = cust.id;
    this.addressService.create(this.newAddress).subscribe({
      next: () => {
        this.toastService.success('New Address Added to Customer', 'Address Added');
        this.loadCustomerAddresses(cust.id);
        this.loadCustomers();
        this.newAddress = { title: '', addressLine: '', customerId: cust.id };
      },
    });
  }

  deleteAddress(addressId: number): void {
    const cust = this.selectedCustomerForAddresses();
    if (!cust) return;

    this.addressService.delete(addressId).subscribe({
      next: () => {
        this.toastService.info('Adress Deleted.', 'Adress Deleted');
        this.loadCustomerAddresses(cust.id);
        this.loadCustomers();
      },
    });
  }
}
