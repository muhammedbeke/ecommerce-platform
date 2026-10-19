import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { CustomerService } from '../../services/customer.service';
import { CategoryService } from '../../services/category.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  const mockProductService = { getAll: () => of({ data: [] }) };
  const mockOrderService = { getAllOrders: () => of([]) };
  const mockCustomerService = { getAll: () => of({ data: [] }) };
  const mockCategoryService = { getAll: () => of({ data: [] }) };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: ProductService, useValue: mockProductService },
        { provide: OrderService, useValue: mockOrderService },
        { provide: CustomerService, useValue: mockCustomerService },
        { provide: CategoryService, useValue: mockCategoryService },
        { provide: ActivatedRoute, useValue: { params: of({}) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
