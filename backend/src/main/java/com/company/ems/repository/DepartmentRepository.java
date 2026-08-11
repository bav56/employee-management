package com.company.ems.repository;

import com.company.ems.entity.Department;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    boolean existsByDepartmentCode(String departmentCode);
    boolean existsByDepartmentName(String departmentName);
}
