import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListOfValuesComponent } from './list-of-values.component';
import { LovService } from '../../services/lov.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('ListOfValuesComponent', () => {
  let component: ListOfValuesComponent;
  let fixture: ComponentFixture<ListOfValuesComponent>;

  const mockLovService = {
    getAll: () => of({ data: [] }),
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
      imports: [ListOfValuesComponent],
      providers: [
        { provide: LovService, useValue: mockLovService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListOfValuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
