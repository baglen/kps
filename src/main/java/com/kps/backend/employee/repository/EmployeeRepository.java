package com.kps.backend.employee.repository;

import com.example.jooq.models.tables.records.EmployeeRecord;
import com.example.jooq.models.tables.records.EmployeeTokenRecord;
import com.example.jooq.models.tables.records.RoleRecord;
import org.jooq.Record;
import org.jooq.Result;

public interface EmployeeRepository {
    EmployeeRecord insertEmployee(EmployeeRecord record);
    Result<Record> getEmployeeByUsername(String username);
    Result<Record> getEmployeeById(Long employeeId);
    Result<Record> getEmployeeByEmail(String email);
    EmployeeTokenRecord getEmployeeToken(Long employeeId);
    Boolean deleteEmployeeTokenById(Long employeeId);
    Boolean deleteEmployeeToken(String token);
    void insertEmployeeToken(Long employeeId, String token);
    Boolean existsByParams(String username, String email);
    RoleRecord getRoleByName(String name);

    Boolean isPerformer(Long employeeId);
    Boolean existsById(Long employeeId);
}
