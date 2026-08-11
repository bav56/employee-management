import { Routes } from '@angular/router';
import { DepartmentList } from './department-list/department-list';
import { EmployeeList } from './employee-list/employee-list';
export const routes: Routes = [
  {
    path: 'departments',
    component: DepartmentList,
  },
  {
    path: 'employees',
    component: EmployeeList,
  }
];
