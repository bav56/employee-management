package com.company.ems.service;

import com.company.ems.entity.Department;
import java.util.List;
public interface DepartmentService {
    List<Department> getAllDepartments();
    Department createDepartment(Department department);
    Department getDepartmentById(Long id);
    Department updateDepartment(Long id, Department department);
    void deleteDepartment(Long id);
}
