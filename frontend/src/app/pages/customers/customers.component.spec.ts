import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomersComponent } from './customers.component';
import { CustomerService } from '../../services/customer.service';
import { AddressService } from '../../services/address.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('CustomersComponent', () => {
  let component: CustomersComponent;
  let fixture: ComponentFixture<CustomersComponent>;

  const mockCustomerService = {
    getAll: () => of({ data: [] }),
    create: () => of({}),
    update: () => of({}),
    delete: () => of({})
  };

  const mockAddressService = {
    getByCustomerId: () => of({ data: [] }),
    create: () => of({}),
    delete: () => of({})
  };

  const mockToastService = {
    success: () => {},
    info: () => {},
    error: () => {},
    warning: () => {}
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomersComponent],
      providers: [
        { provide: CustomerService, useValue: mockCustomerService },
        { provide: AddressService, useValue: mockAddressService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CustomersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
