import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BuyingsComponent } from './buyings.component';
import { BuyingService } from '../../services/buying.service';
import { ProductService } from '../../services/product.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('BuyingsComponent', () => {
  let component: BuyingsComponent;
  let fixture: ComponentFixture<BuyingsComponent>;

  const mockBuyingService = {
    getAll: () => of({ data: [] }),
    create: () => of({}),
    delete: () => of({})
  };

  const mockProductService = {
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
      imports: [BuyingsComponent],
      providers: [
        { provide: BuyingService, useValue: mockBuyingService },
        { provide: ProductService, useValue: mockProductService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BuyingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
