package com.company.ems.controller;

import com.company.ems.dto.EmployeeProjectsResponse;
import com.company.ems.entity.Employee;
import com.company.ems.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeProjectsResponse> getAllEmployees() {

        return employeeService.getEmployeesWithProjects();
    }

    @GetMapping("/page")
    public Page<EmployeeProjectsResponse> getEmployeesPage(
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "5") String size) {

        if (!page.matches("[0-9]+")) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page must be a number");
        }

        if (!size.matches("[0-9]+")) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be a number");
        }

        int pageNumber;
        int pageSize;

        try {

            pageNumber = Integer.parseInt(page);
            pageSize = Integer.parseInt(size);

        } catch (NumberFormatException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be valid numbers");
        }


        if (pageSize < 1) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Size must be greater than 0");
        }


        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("lastName").ascending());


        return employeeService.getEmployeesWithProjects(pageable);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable String id) {

        Long employeeId = convertEmployeeId(id);

        return employeeService.getEmployeeById(employeeId);
    }


    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee employee) {

        return employeeService.createEmployee(employee);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {

        Long employeeId = convertEmployeeId(id);

        return employeeService.updateEmployee(employeeId, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {

        Long employeeId = convertEmployeeId(id);

        employeeService.deleteEmployee(employeeId);
    }

    private Long convertEmployeeId(String id) {

        if (!id.matches("[0-9]+")) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID must be a number");
        }


        try {

            return Long.parseLong(id);

        } catch (NumberFormatException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID is not valid");
        }
    }
}