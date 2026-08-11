package com.company.ems.repository;

import com.company.ems.entity.Employee;
import com.company.ems.dto.EmployeeProjectRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    @Query(
            value = """
                    SELECT
                        e.employee_id AS "employeeId",
                        e.employee_code AS "employeeCode",
                        e.first_name AS "firstName",
                        e.last_name AS "lastName",
                        p.id AS "projectId",
                        p.name AS "projectName"
                    FROM employee e
                    LEFT JOIN project_employee pe
                        ON e.employee_id = pe.employee_id
                    LEFT JOIN project p
                        ON pe.project_id = p.id
                    ORDER BY e.employee_id, p.id
                    """,
            nativeQuery = true
    )
    List<EmployeeProjectRow> findEmployeesWithProjects();
}