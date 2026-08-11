package com.company.ems.service.impl;

import com.company.ems.entity.Department;
import com.company.ems.repository.DepartmentRepository;
import com.company.ems.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getAllDepartments() {

        return departmentRepository.findAll();
    }

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByDepartmentCode(department.getDepartmentCode())) {
            throw new RuntimeException("Department code already exists");
        }

        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            throw new RuntimeException("Department name already exists");
        }

        if (!department.getStatus().equals("ACTIVE") && !department.getStatus().equals("INACTIVE")) {
            throw new RuntimeException("Status must be ACTIVE or INACTIVE");
        }
        return departmentRepository.save(department);
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id){
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {

        Department existingDepartment = departmentRepository.findById(id).orElse(null);

        if (existingDepartment != null) {
            existingDepartment.setDepartmentCode(department.getDepartmentCode());
            existingDepartment.setDepartmentName(department.getDepartmentName());
            existingDepartment.setStatus(department.getStatus());

            return departmentRepository.save(existingDepartment);
        }

        return null;
    }

    @Override
    public void deleteDepartment(Long id){
        departmentRepository.deleteById(id);
    }
}