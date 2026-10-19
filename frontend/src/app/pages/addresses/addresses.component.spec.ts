import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddressesComponent } from './addresses.component';
import { AddressService } from '../../services/address.service';
import { CustomerService } from '../../services/customer.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('AddressesComponent', () => {
  let component: AddressesComponent;
  let fixture: ComponentFixture<AddressesComponent>;

  const mockAddressService = {
    getAll: () => of({ data: [] }),
    create: () => of({}),
    update: () => of({}),
    delete: () => of({})
  };

  const mockCustomerService = {
    getAll: () => of({ data: [] })
  };

  const mockToastService = {
    success: () => {},
    info: () => {},
    error: () => {},
    warning: () => {}
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddressesComponent],
      providers: [
        { provide: AddressService, useValue: mockAddressService },
        { provide: CustomerService, useValue: mockCustomerService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AddressesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
