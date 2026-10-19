import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RoleService } from '../../services/role.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmService } from '../../services/confirm.service';
import { RoleDto, RoleRequest } from '../../models/api.models';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.css',
})
export class RolesComponent implements OnInit {
  roles = signal<RoleDto[]>([]);

  showModal = signal(false);
  editingRole = signal<RoleDto | null>(null);
  formData: RoleRequest = { name: '' };

  constructor(
    private roleService: RoleService,
    private toastService: ToastService,
    private confirmService: ConfirmService,
  ) {}

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(): void {
    this.roleService.getAll().subscribe({
      next: (res) => this.roles.set(res.data || []),
    });
  }

  openCreateModal(): void {
    this.editingRole.set(null);
    this.formData = { name: '' };
    this.showModal.set(true);
  }

  openEditModal(role: RoleDto): void {
    this.editingRole.set(role);
    this.formData = { name: role.name };
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveRole(): void {
    if (!this.formData.name) return;

    if (this.editingRole()) {
      this.roleService.update(this.editingRole()!.id, this.formData).subscribe({
        next: () => {
          this.toastService.success('Rol Updated Successfully.', 'Role Updated');
          this.loadRoles();
          this.closeModal();
        },
      });
    } else {
      this.roleService.create(this.formData).subscribe({
        next: () => {
          this.toastService.success('New System Role Added.', 'Role Added');
          this.loadRoles();
          this.closeModal();
        },
      });
    }
  }

  async deleteRole(id: number): Promise<void> {
    const confirmed = await this.confirmService.confirm({
      title: 'Delete Role',
      message: 'Are you sure you want to delete this role?',
      confirmText: 'Delete',
      btnClass: 'btn-danger',
    });
    if (confirmed) {
      this.roleService.delete(id).subscribe({
        next: () => {
          this.toastService.info('Role Deleted.', 'Role Deleted');
          this.loadRoles();
        },
      });
    }
  }
}
