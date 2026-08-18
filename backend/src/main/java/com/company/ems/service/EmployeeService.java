package com.company.ems.service;

import com.company.ems.dto.EmployeeProjectsResponse;
import com.company.ems.entity.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

    List<EmployeeProjectsResponse> getEmployeesWithProjects();

    Page<EmployeeProjectsResponse> getEmployeesWithProjects(Pageable pageable);
}