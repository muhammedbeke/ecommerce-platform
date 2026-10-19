import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StocksComponent } from './stocks.component';
import { ProductService } from '../../services/product.service';
import { StockService } from '../../services/stock.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('StocksComponent', () => {
  let component: StocksComponent;
  let fixture: ComponentFixture<StocksComponent>;

  const mockProductService = {
    getAll: () => of({ data: [] })
  };

  const mockStockService = {
    updateStock: () => of({}),
    setStock: () => of({})
  };

  const mockToastService = {
    success: () => {},
    info: () => {},
    error: () => {},
    warning: () => {}
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StocksComponent],
      providers: [
        { provide: ProductService, useValue: mockProductService },
        { provide: StockService, useValue: mockStockService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(StocksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
