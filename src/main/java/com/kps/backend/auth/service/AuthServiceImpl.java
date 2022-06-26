package com.kps.backend.auth.service;

import com.example.jooq.models.tables.records.CompanyRecord;
import com.example.jooq.models.tables.records.CompanySettingsRecord;
import com.example.jooq.models.tables.records.EmployeeRecord;
import com.example.jooq.models.tables.records.RoleRecord;
import com.kps.backend.auth.request.LoginRequest;
import com.kps.backend.auth.request.RegisterRequest;
import com.kps.backend.auth.response.RegisterResponse;
import com.kps.backend.auth.response.TokenResponse;
import com.kps.backend.company.repository.CompanyRepository;
import com.kps.backend.employee.repository.EmployeeRepository;
import com.kps.backend.exception.BadRequestException;
import com.kps.backend.exception.ObjectAlreadyExistsException;
import com.kps.backend.exception.ObjectNotFoundException;
import com.kps.backend.exception.RestrictedException;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.exception.AuthorizationException;
import com.kps.backend.security.model.EmployeeModel;
import com.kps.backend.security.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService{

    private final JwtUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper mapper;

    public AuthServiceImpl(JwtUtil jwtTokenUtil, @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager, EmployeeRepository employeeRepository,
                           CompanyRepository companyRepository, ModelMapper mapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = null;
        if(authentication.getPrincipal() instanceof EmployeeModel){
            employeeModel = (EmployeeModel) authentication.getPrincipal();
            if(employeeModel.getRoleRecord().getName().equals("performer")){
                throw new RestrictedException("Forbidden");
            }
        }
        if(employeeRepository.existsByParams(request.getLogin(), request.getEmail())){
            throw new ObjectAlreadyExistsException("User with that username, email or phone already exists");
        }
        RoleRecord role = employeeRepository.getRoleByName(request.getRole());
        if(role == null){
            throw new ObjectNotFoundException("Role with name: " + request.getRole() + " does not exist");
        }
        EmployeeRecord employeeRecord = mapper.map(request, EmployeeRecord.class);
        employeeRecord.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        employeeRecord.setCreated(Instant.now().getEpochSecond());
        employeeRecord.setRoleId(role.getId());
        if(role.getName().equals("admin")){
            if(request.getCompanyName() == null){
                throw new BadRequestException("Company name was empty");
            }
            if(companyRepository.existsByName(request.getCompanyName())){
                throw new ObjectAlreadyExistsException("Company with requested name already exists");
            }
            if(request.getMaxCloudSize() != null && request.getMaxPhotoCount() != null && request.getMaxTemplateCount() != null){
                CompanyRecord companyRecord = new CompanyRecord();
                companyRecord.setName(request.getCompanyName());
                companyRecord.setCreated(Instant.now().getEpochSecond());
                companyRecord = companyRepository.insert(companyRecord);
                CompanySettingsRecord settingsRecord = new CompanySettingsRecord();
                settingsRecord.setMaxCloudSize(request.getMaxCloudSize());
                settingsRecord.setMaxPhotoCount(request.getMaxPhotoCount());
                settingsRecord.setMaxTemplateCount(request.getMaxTemplateCount());
                settingsRecord.setCompanyId(companyRecord.getId());
                settingsRecord = companyRepository.insertSettings(settingsRecord);
                employeeRecord.setCompanyId(companyRecord.getId());
            } else {
                throw new BadRequestException("Company info should be applied");
            }
        }
        if(employeeRecord.getCompanyId() == null && employeeModel != null){
            employeeRecord.setCompanyId(employeeModel.getEmployeeRecord().getCompanyId());
        }
        employeeRecord = employeeRepository.insertEmployee(employeeRecord);
        RegisterResponse registerResponse = mapper.map(employeeRecord, RegisterResponse.class);
        registerResponse.setRole(role.getName());
        return registerResponse;
    }

    @Override
    public TokenResponse login(LoginRequest request) throws AuthorizationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException | LockedException e ) {
            throw new AuthorizationException("Invalid login or password");
        }
        EmployeeModel userDetails = (EmployeeModel) userDetailsService.loadUserByUsername(request.getUsername());
        if(userDetails.getRoleRecord().getName().equals("performer")){
            throw new AuthorizationException("Use mobile login url");
        }
        TokenResponse tokenResponse = new TokenResponse(jwtTokenUtil.generateToken(userDetails), userDetails.getEmployeeRecord().getId());
        employeeRepository.deleteEmployeeTokenById(userDetails.getEmployeeRecord().getId());
        employeeRepository.insertEmployeeToken(tokenResponse.getUserId(), tokenResponse.getToken());
        return tokenResponse;
    }

    @Override
    public TokenResponse mobileLogin(LoginRequest request) throws AuthorizationException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException | LockedException e ) {
            throw new AuthorizationException("Invalid login or password");
        }
        EmployeeModel userDetails = (EmployeeModel) userDetailsService.loadUserByUsername(request.getUsername());
        if(!userDetails.getRoleRecord().getName().equals("performer")){
            throw new AuthorizationException("Only performer can access this layer");
        }
        TokenResponse tokenResponse = new TokenResponse(jwtTokenUtil.generateToken(userDetails), userDetails.getEmployeeRecord().getId());
        employeeRepository.deleteEmployeeTokenById(userDetails.getEmployeeRecord().getId());
        employeeRepository.insertEmployeeToken(tokenResponse.getUserId(), tokenResponse.getToken());
        return tokenResponse;
    }

    @Override
    public SuccessResponse logout(String token) {
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        if(employeeRepository.deleteEmployeeToken(token)){
            return new SuccessResponse(true);
        } else {
            throw new ObjectNotFoundException("User's token not found");
        }
    }
}
