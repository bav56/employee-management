package com.company.ems.dto;

import com.company.ems.entity.Department;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeProjectsResponse {
    private Long employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private Department department;
    private Long designationId;
    private String email;
    private String mobile;
    private LocalDate joiningDate;
    private String employmentType;
    private String status;

    private List<ProjectResponse> projects = new ArrayList<>();

    public EmployeeProjectsResponse(
            Long employeeId,
            String employeeCode,
            String firstName,
            String lastName,
            String gender,
            LocalDate dateOfBirth,
            Department department,
            Long designationId,
            String email,
            String mobile,
            LocalDate joiningDate,
            String employmentType,
            String status) {

        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.department = department;
        this.designationId = designationId;
        this.email = email;
        this.mobile = mobile;
        this.joiningDate = joiningDate;
        this.employmentType = employmentType;
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Department getDepartment() {
        return department;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getStatus() {
        return status;
    }

    public List<ProjectResponse> getProjects() {
        return projects;
    }
}
