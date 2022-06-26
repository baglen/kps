package com.kps.backend.employee.repository;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.EmployeeRecord;
import com.example.jooq.models.tables.records.EmployeeTokenRecord;
import com.example.jooq.models.tables.records.RoleRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository{

    private final DSLContext context;

    public EmployeeRepositoryImpl(DSLContext context) {
        this.context = context;
    }


    @Override
    public EmployeeRecord insertEmployee(EmployeeRecord record) {
        return context.insertInto(Tables.EMPLOYEE)
                .set(record)
                .returning()
                .fetchOne();
    }

    @Override
    public Result<Record> getEmployeeByUsername(String username) {
        return context.select(Tables.EMPLOYEE.fields())
                .select(Tables.ROLE.fields())
                .from(Tables.EMPLOYEE)
                .join(Tables.ROLE).on(Tables.EMPLOYEE.ROLE_ID.eq(Tables.ROLE.ID))
                .where(Tables.EMPLOYEE.LOGIN.eq(username))
                .fetch();
    }

    @Override
    public Result<Record> getEmployeeById(Long employeeId) {
        return context.select(Tables.EMPLOYEE.fields())
                .select(Tables.ROLE.fields())
                .from(Tables.EMPLOYEE)
                .join(Tables.ROLE).on(Tables.EMPLOYEE.ROLE_ID.eq(Tables.ROLE.ID))
                .where(Tables.EMPLOYEE.ID.eq(employeeId))
                .fetch();
    }

    @Override
    public Result<Record> getEmployeeByEmail(String email) {
        return context.select(Tables.EMPLOYEE.fields())
                .select(Tables.ROLE.fields())
                .from(Tables.EMPLOYEE)
                .join(Tables.ROLE).on(Tables.EMPLOYEE.ROLE_ID.eq(Tables.ROLE.ID))
                .where(Tables.EMPLOYEE.EMAIL.eq(email))
                .fetch();
    }

    @Override
    public EmployeeTokenRecord getEmployeeToken(Long employeeId) {
        return context.selectFrom(Tables.EMPLOYEE_TOKEN)
                .where(Tables.EMPLOYEE_TOKEN.EMPLOYEE_ID.eq(employeeId))
                .fetchOne();
    }

    @Override
    public Boolean deleteEmployeeTokenById(Long employeeId) {
        return context.deleteFrom(Tables.EMPLOYEE_TOKEN)
                .where(Tables.EMPLOYEE_TOKEN.EMPLOYEE_ID.eq(employeeId))
                .execute() == 1;
    }

    @Override
    public Boolean deleteEmployeeToken(String token) {
        return context.deleteFrom(Tables.EMPLOYEE_TOKEN)
                .where(Tables.EMPLOYEE_TOKEN.TOKEN.eq(token))
                .execute() == 1;
    }

    @Override
    public void insertEmployeeToken(Long employeeId, String token) {
        context.insertInto(Tables.EMPLOYEE_TOKEN)
                .set(Tables.EMPLOYEE_TOKEN.EMPLOYEE_ID, employeeId)
                .set(Tables.EMPLOYEE_TOKEN.TOKEN, token)
                .set(Tables.EMPLOYEE_TOKEN.CREATED, Instant.now().getEpochSecond())
                .execute();
    }

    @Override
    public Boolean existsByParams(String username, String email) {
        return context.selectFrom(Tables.EMPLOYEE)
                .where(Tables.EMPLOYEE.LOGIN.eq(username))
                .or(Tables.EMPLOYEE.EMAIL.eq(email))
                .execute() > 0;
    }

    @Override
    public RoleRecord getRoleByName(String name) {
        return context.selectFrom(Tables.ROLE)
                .where(Tables.ROLE.NAME.eq(name))
                .fetchOne();
    }

    @Override
    public Boolean isPerformer(Long employeeId) {
        return context.select(Tables.EMPLOYEE.fields())
                .select(Tables.ROLE.fields())
                .from(Tables.EMPLOYEE)
                .join(Tables.ROLE).on(Tables.EMPLOYEE.ROLE_ID.eq(Tables.ROLE.ID))
                .where(Tables.EMPLOYEE.ID.eq(employeeId))
                .and(Tables.ROLE.NAME.eq("performer"))
                .execute() == 1;
    }

    @Override
    public Boolean existsById(Long employeeId) {
        return context.selectFrom(Tables.EMPLOYEE)
                .where(Tables.EMPLOYEE.ID.eq(employeeId))
                .execute() == 1;
    }
}
