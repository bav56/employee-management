package com.company.ems.service.impl;

import com.company.ems.dto.EmployeeProjectRow;
import com.company.ems.dto.EmployeeProjectsResponse;
import com.company.ems.dto.ProjectResponse;

import com.company.ems.entity.Employee;
import com.company.ems.entity.Department;

import com.company.ems.repository.EmployeeRepository;
import com.company.ems.repository.DepartmentRepository;

import com.company.ems.service.EmployeeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;


    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {

        this.employeeRepository = employeeRepository;

        this.departmentRepository = departmentRepository;
    }


    @Override
    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }


    @Override
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id).orElse(null);
    }


    @Override
    public Employee createEmployee(
            Employee employee) {

        if (employeeRepository.existsByEmployeeCode(employee.getEmployeeCode())) {

            throw new RuntimeException("Employee code already exists");
        }


        if (employeeRepository.existsByEmail(employee.getEmail())) {

            throw new RuntimeException("Email already exists");
        }


        if (employeeRepository.existsByMobile(employee.getMobile())) {

            throw new RuntimeException("Mobile number already exists");
        }


        Long departmentId =
                employee
                        .getDepartment()
                        .getDepartmentId();


        Department department =
                departmentRepository
                        .findById(departmentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Department does not exist"
                                )
                        );


        employee.setDepartment(department);


        return employeeRepository
                .save(employee);
    }


    @Override
    public Employee updateEmployee(
            Long id,
            Employee employee) {

        Employee existingEmployee =
                employeeRepository
                        .findById(id)
                        .orElse(null);


        if (existingEmployee == null) {

            return null;
        }


        Long departmentId =
                employee
                        .getDepartment()
                        .getDepartmentId();


        Department department =
                departmentRepository
                        .findById(departmentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Department does not exist"
                                )
                        );


        existingEmployee.setFirstName(
                employee.getFirstName()
        );

        existingEmployee.setLastName(
                employee.getLastName()
        );

        existingEmployee.setGender(
                employee.getGender()
        );

        existingEmployee.setDateOfBirth(
                employee.getDateOfBirth()
        );

        existingEmployee.setDepartment(
                department
        );

        existingEmployee.setDesignationId(
                employee.getDesignationId()
        );

        existingEmployee.setEmail(
                employee.getEmail()
        );

        existingEmployee.setMobile(
                employee.getMobile()
        );

        existingEmployee.setJoiningDate(
                employee.getJoiningDate()
        );

        existingEmployee.setEmploymentType(
                employee.getEmploymentType()
        );

        existingEmployee.setStatus(
                employee.getStatus()
        );


        return employeeRepository
                .save(existingEmployee);
    }


    @Override
    public void deleteEmployee(Long id) {

        Employee employee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Employee does not exist"
                                )
                        );


        employee.setStatus("INACTIVE");


        employeeRepository
                .save(employee);
    }


    // NORMAL EMPLOYEE + PROJECT LIST
    @Override
    public List<EmployeeProjectsResponse>
    getEmployeesWithProjects() {

        List<Employee> employees =
                employeeRepository.findAll();


        List<EmployeeProjectRow> rows =
                employeeRepository
                        .findEmployeesWithProjects();


        Map<Long, EmployeeProjectsResponse>
                employeeMap =
                new LinkedHashMap<>();


        for (Employee employee : employees) {

            EmployeeProjectsResponse response =
                    createEmployeeResponse(employee);


            employeeMap.put(
                    employee.getEmployeeId(),
                    response
            );
        }


        addProjects(
                employeeMap,
                rows
        );


        return new ArrayList<>(
                employeeMap.values()
        );
    }


    // PAGED EMPLOYEE + PROJECT LIST
    @Override
    public Page<EmployeeProjectsResponse>
    getEmployeesWithProjects(
            Pageable pageable) {

        Page<Employee> employeePage =
                employeeRepository
                        .findAll(pageable);


        List<Employee> employees =
                employeePage.getContent();


        List<EmployeeProjectRow> rows =
                employeeRepository
                        .findEmployeesWithProjects();


        Map<Long, EmployeeProjectsResponse>
                employeeMap =
                new LinkedHashMap<>();


        for (Employee employee : employees) {

            EmployeeProjectsResponse response =
                    createEmployeeResponse(employee);


            employeeMap.put(
                    employee.getEmployeeId(),
                    response
            );
        }


        addProjects(
                employeeMap,
                rows
        );


        List<EmployeeProjectsResponse>
                employeeResponses =
                new ArrayList<>(
                        employeeMap.values()
                );


        return new PageImpl<>(
                employeeResponses,
                pageable,
                employeePage.getTotalElements()
        );
    }


    // CREATES THE RESPONSE OBJECT
    private EmployeeProjectsResponse
    createEmployeeResponse(
            Employee employee) {

        return new EmployeeProjectsResponse(

                employee.getEmployeeId(),

                employee.getEmployeeCode(),

                employee.getFirstName(),

                employee.getLastName(),

                employee.getGender(),

                employee.getDateOfBirth(),

                employee.getDepartment(),

                employee.getDesignationId(),

                employee.getEmail(),

                employee.getMobile(),

                employee.getJoiningDate(),

                employee.getEmploymentType(),

                employee.getStatus()
        );
    }


    // ADDS PROJECTS TO EACH EMPLOYEE
    private void addProjects(
            Map<Long, EmployeeProjectsResponse>
                    employeeMap,
            List<EmployeeProjectRow> rows) {

        for (EmployeeProjectRow row : rows) {

            EmployeeProjectsResponse employee =
                    employeeMap.get(
                            row.getEmployeeId()
                    );


            if (
                    employee != null &&
                            row.getProjectId() != null
            ) {

                employee
                        .getProjects()
                        .add(
                                new ProjectResponse(
                                        row.getProjectId(),
                                        row.getProjectName()
                                )
                        );
            }
        }
    }
}