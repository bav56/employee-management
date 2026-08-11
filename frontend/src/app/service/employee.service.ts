import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Project {
  projectId: number;
  projectName: string;
}

export interface Employee {
  employeeId?: number;
  employeeCode: string;
  firstName: string;
  lastName: string;
  gender: string;
  dateOfBirth: string;

  department: {
    departmentId: number;
    departmentCode?: string;
    departmentName?: string;
    status?: string;
  };

  designationId: number;
  email: string;
  mobile: string;
  joiningDate: string;
  employmentType: string;
  status: string;

  projects?: Project[];
}

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {

  private apiUrl = 'http://localhost:8080/api/v1/employees';

  constructor(private http: HttpClient) {}

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.apiUrl);
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, employee);
  }

  updateEmployee(id: number, employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
