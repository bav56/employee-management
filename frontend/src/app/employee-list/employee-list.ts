import { Component, OnInit, ChangeDetectorRef } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Employee, EmployeeService } from '../service/employee.service';
import { Department, DepartmentService } from '../service/department.service';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.css',
})
export class EmployeeList implements OnInit {
  employees: Employee[] = [];
  departments: Department[] = [];

  newEmployee: Employee = {
    employeeCode: '',
    firstName: '',
    lastName: '',
    gender: 'MALE',
    dateOfBirth: '',
    department: {
      departmentId: 0,
    },
    designationId: 1,
    email: '',
    mobile: '',
    joiningDate: '',
    employmentType: 'PERMANENT',
    status: 'ACTIVE',
    projects: [],
  };

  editEmployee: Employee | null = null;

  message = '';
  isSubmitting = false;

  constructor(
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private changeDetector: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
    this.loadDepartments();
  }

  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: Employee[]) => {
        this.employees = data;
        this.changeDetector.detectChanges();
      },
      error: (error: any) => {
        console.error('Error loading employees', error);
      },
    });
  }

  loadDepartments(): void {
    this.departmentService.getAllDepartments().subscribe({
      next: (data: Department[]) => {
        this.departments = data;
        this.changeDetector.detectChanges();
      },
      error: (error: any) => {
        console.error('Error loading departments', error);
      },
    });
  }

  saveEmployee(): void {
    if (this.isSubmitting) {
      return;
    }

    if (!this.areDatesValid()) {
      this.message = 'Please correct the date fields.';
      return;
    }

    if (this.newEmployee.department.departmentId === 0) {
      this.message = 'Please select a department.';
      return;
    }

    this.isSubmitting = true;

    this.employeeService.createEmployee(this.newEmployee).subscribe({
      next: () => {
        this.message = 'Employee added successfully';

        this.newEmployee = {
          employeeCode: '',
          firstName: '',
          lastName: '',
          gender: 'MALE',
          dateOfBirth: '',
          department: {
            departmentId: 0,
          },
          designationId: 1,
          email: '',
          mobile: '',
          joiningDate: '',
          employmentType: 'PERMANENT',
          status: 'ACTIVE',
          projects: [],
        };

        this.isSubmitting = false;

        this.loadEmployees();
      },

      error: (error: any) => {
        console.error('Error creating employee', error);

        this.isSubmitting = false;

        if (typeof error.error === 'string') {
          this.message = error.error;
        } else {
          this.message = 'Could not add employee';
        }
      },
    });
  }

  startEdit(employee: Employee): void {
    this.editEmployee = {
      ...employee,

      department: {
        ...employee.department,
      },

      projects: employee.projects ? [...employee.projects] : [],
    };
  }

  cancelEdit(): void {
    this.editEmployee = null;
    this.message = '';
  }

  updateEmployee(): void {
    if (!this.editEmployee?.employeeId) {
      return;
    }

    if (!this.areEditDatesValid()) {
      this.message = 'Please correct the date fields.';
      return;
    }

    if (this.editEmployee.department.departmentId === 0) {
      this.message = 'Please select a department.';
      return;
    }

    this.employeeService.updateEmployee(this.editEmployee.employeeId, this.editEmployee).subscribe({
      next: () => {
        this.message = 'Employee updated successfully';

        this.editEmployee = null;

        this.loadEmployees();
      },

      error: (error: any) => {
        console.error('Error updating employee', error);

        if (typeof error.error === 'string') {
          this.message = error.error;
        } else {
          this.message = 'Could not update employee';
        }
      },
    });
  }

  deleteEmployee(id: number | undefined): void {
    if (!id) {
      return;
    }

    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.message = 'Employee deactivated successfully';
        this.loadEmployees();
      },

      error: (error: any) => {
        console.error('Error deleting employee', error);
        this.message = 'Could not deactivate employee';
      },
    });
  }

  getToday(): string {
    return new Date().toISOString().split('T')[0];
  }

  getMaximumDob(): string {
    const today = new Date();

    today.setFullYear(today.getFullYear() - 18);

    return today.toISOString().split('T')[0];
  }

  isEmployeeAtLeast18(): boolean {
    if (!this.newEmployee.dateOfBirth) {
      return false;
    }

    return this.isAtLeast18(this.newEmployee.dateOfBirth);
  }

  isEditEmployeeAtLeast18(): boolean {
    if (!this.editEmployee?.dateOfBirth) {
      return false;
    }

    return this.isAtLeast18(this.editEmployee.dateOfBirth);
  }

  private isAtLeast18(dateOfBirth: string): boolean {
    const dob = new Date(dateOfBirth);
    const today = new Date();

    let age = today.getFullYear() - dob.getFullYear();

    const monthDifference = today.getMonth() - dob.getMonth();

    if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < dob.getDate())) {
      age--;
    }

    return age >= 18;
  }

  areDatesValid(): boolean {
    if (!this.newEmployee.dateOfBirth || !this.newEmployee.joiningDate) {
      return false;
    }

    const dob = new Date(this.newEmployee.dateOfBirth);

    const joiningDate = new Date(this.newEmployee.joiningDate);

    const today = new Date(this.getToday());

    return dob <= today && joiningDate >= dob && this.isEmployeeAtLeast18();
  }

  areEditDatesValid(): boolean {
    if (!this.editEmployee || !this.editEmployee.dateOfBirth || !this.editEmployee.joiningDate) {
      return false;
    }

    const dob = new Date(this.editEmployee.dateOfBirth);

    const joiningDate = new Date(this.editEmployee.joiningDate);

    const today = new Date(this.getToday());

    return (
      dob <= today && joiningDate >= dob &&  this.isEditEmployeeAtLeast18()
    );
  }
}
