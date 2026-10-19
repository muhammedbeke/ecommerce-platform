import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrdersComponent } from './orders.component';
import { OrderService } from '../../services/order.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('OrdersComponent', () => {
  let component: OrdersComponent;
  let fixture: ComponentFixture<OrdersComponent>;

  const mockOrderService = {
    getAllOrders: () => of([]),
    advanceStatus: () => of({}),
    cancelOrder: () => of({}),
    deleteOrder: () => of({})
  };

  const mockToastService = {
    success: () => {},
    info: () => {},
    error: () => {},
    warning: () => {}
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrdersComponent],
      providers: [
        { provide: OrderService, useValue: mockOrderService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
