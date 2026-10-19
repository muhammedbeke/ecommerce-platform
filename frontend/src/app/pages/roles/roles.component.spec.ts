import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RolesComponent } from './roles.component';
import { RoleService } from '../../services/role.service';
import { ToastService } from '../../services/toast.service';
import { of } from 'rxjs';

describe('RolesComponent', () => {
  let component: RolesComponent;
  let fixture: ComponentFixture<RolesComponent>;

  const mockRoleService = {
    getAll: () => of({ data: [] }),
    create: () => of({}),
    update: () => of({}),
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
      imports: [RolesComponent],
      providers: [
        { provide: RoleService, useValue: mockRoleService },
        { provide: ToastService, useValue: mockToastService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RolesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
