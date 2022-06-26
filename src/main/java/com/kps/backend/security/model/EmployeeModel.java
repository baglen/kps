package com.kps.backend.security.model;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.EmployeeRecord;
import com.example.jooq.models.tables.records.RoleRecord;
import org.jooq.Record;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class EmployeeModel implements UserDetails {
    private EmployeeRecord employeeRecord;
    private RoleRecord roleRecord;

    public EmployeeModel(Record record) {
        employeeRecord = record.into(Tables.EMPLOYEE);
        roleRecord = record.into(Tables.ROLE);
    }

    public EmployeeRecord getEmployeeRecord() {
        return employeeRecord;
    }

    public void setEmployeeRecord(EmployeeRecord employeeRecord) {
        this.employeeRecord = employeeRecord;
    }

    public RoleRecord getRoleRecord() {
        return roleRecord;
    }

    public void setRoleRecord(RoleRecord roleRecord) {
        this.roleRecord = roleRecord;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleRecord.getName()));
    }

    @Override
    public String getPassword() {
        return employeeRecord.getPassword();
    }

    @Override
    public String getUsername() {
        return employeeRecord.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
