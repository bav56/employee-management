import {
  Component,
  OnInit,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  Department,
  DepartmentService
} from '../service/department.service';

@Component({
  selector: 'app-department-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './department-list.html',
  styleUrl: './department-list.css'
})
export class DepartmentList implements OnInit {

  departments: Department[] = [];

  newDepartment: Department = {
    departmentCode: '',
    departmentName: '',
    status: 'ACTIVE'
  };

  editDepartment: Department | null = null;

  message = '';

  constructor(
    private departmentService: DepartmentService,
    private changeDetector: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {
    this.departmentService.getAllDepartments().subscribe({
      next: (data: Department[]) => {
        this.departments = data;
        this.changeDetector.detectChanges();
      },
      error: (error: any) => {
        console.error('Error loading departments', error);
      }
    });
  }

  saveDepartment(): void {
    this.departmentService.createDepartment(this.newDepartment).subscribe({
      next: () => {
        this.message = 'Department added successfully';

        this.newDepartment = {
          departmentCode: '',
          departmentName: '',
          status: 'ACTIVE'
        };

        this.loadDepartments();
      },
      error: (error: any) => {
        console.error('Error creating department', error);
        this.message = 'Could not add department';
      }
    });
  }

  startEdit(department: Department): void {
    this.editDepartment = {
      ...department
    };
  }

  cancelEdit(): void {
    this.editDepartment = null;
  }

  updateDepartment(): void {
    if (!this.editDepartment?.departmentId) {
      return;
    }

    this.departmentService
      .updateDepartment(
        this.editDepartment.departmentId,
        this.editDepartment
      )
      .subscribe({
        next: () => {
          this.message = 'Department updated successfully';
          this.editDepartment = null;
          this.loadDepartments();
        },
        error: (error: any) => {
          console.error('Error updating department', error);
          this.message = 'Could not update department';
        }
      });
  }

  deleteDepartment(id: number | undefined): void {
    if (!id) {
      return;
    }

    this.departmentService.deleteDepartment(id).subscribe({
      next: () => {
        this.message = 'Department deleted successfully';
        this.loadDepartments();
      },
      error: (error: any) => {
        console.error('Error deleting department', error);
        this.message = 'Could not delete department';
      }
    });
  }
}
