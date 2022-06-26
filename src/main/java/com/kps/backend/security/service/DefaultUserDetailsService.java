package com.kps.backend.security.service;

import com.kps.backend.employee.repository.EmployeeRepository;
import com.kps.backend.security.model.EmployeeModel;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public DefaultUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<Record> record = employeeRepository.getEmployeeByUsername(username);
        if(record.size() == 0){
            throw new UsernameNotFoundException("User not found");
        }
        return new EmployeeModel(record.get(0));
    }
}