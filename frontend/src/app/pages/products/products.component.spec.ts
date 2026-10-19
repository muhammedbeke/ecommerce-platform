import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductsComponent } from './products.component';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { ImageService } from '../../services/image.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('ProductsComponent', () => {
  let component: ProductsComponent;
  let fixture: ComponentFixture<ProductsComponent>;

  const mockProductService = {
    getAll: () => of({ data: [] }),
    getById: () => of({ data: {} }),
    create: () => of({}),
    update: () => of({}),
    delete: () => of({})
  };

  const mockCategoryService = {
    getAll: () => of({ data: [] })
  };

  const mockImageService = {
    uploadImages: () => of({}),
    deleteImage: () => of({}),
    getImageDownloadUrl: (id: number) => `http://localhost:8080/api/v1/images/${id}`
  };

  const mockToastService = {
    success: () => {},
    info: () => {},
    error: () => {},
    warning: () => {}
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsComponent],
      providers: [
        { provide: ProductService, useValue: mockProductService },
        { provide: CategoryService, useValue: mockCategoryService },
        { provide: ImageService, useValue: mockImageService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
