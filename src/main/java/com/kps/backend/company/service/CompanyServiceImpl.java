package com.kps.backend.company.service;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.pojos.Company;
import com.example.jooq.models.tables.records.CompanyRecord;
import com.example.jooq.models.tables.records.CompanySettingsRecord;
import com.kps.backend.company.repository.CompanyRepository;
import com.kps.backend.company.request.EditCompanyRequest;
import com.kps.backend.company.response.CompanyResponse;
import com.kps.backend.company.response.CompanySettingsModel;
import com.kps.backend.exception.BadRequestException;
import com.kps.backend.exception.RestrictedException;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.model.EmployeeModel;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final ModelMapper mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper mapper) {
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    @Override
    public CompanyResponse edit(EditCompanyRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        if(!employeeModel.getEmployeeRecord().getCompanyId().equals(request.getCompanyId())){
            throw new RestrictedException("That's not your company");
        }
        CompanyRecord companyRecord = companyRepository.getById(request.getCompanyId());
        if(companyRecord == null){
            throw new BadRequestException("Requested company does not exist");
        }
        if(!companyRecord.getName().equals(request.getName())){
            if(companyRepository.existsByName(request.getName())){
                throw new BadRequestException("Company with requested name already exists");
            }
        }
        companyRecord.setName(request.getName());
        companyRecord = companyRepository.edit(companyRecord);
        CompanySettingsRecord settingsRecord = new CompanySettingsRecord();
        settingsRecord.setCompanyId(companyRecord.getId());
        settingsRecord.setMaxTemplateCount(request.getMaxTemplateCount());
        settingsRecord.setMaxCloudSize(request.getMaxCloudSize());
        settingsRecord.setMaxPhotoCount(request.getMaxPhotoCount());
        settingsRecord = companyRepository.editSettings(settingsRecord);
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setCompany(companyRecord.into(Company.class));
        CompanySettingsModel settingsModel = settingsRecord.into(CompanySettingsModel.class);
        companyResponse.setSettings(settingsModel);
        return companyResponse;
    }

    @Override
    public CompanyResponse getById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        Result<Record> companyInfo = companyRepository.getCompanyWithSettings(employeeModel.getEmployeeRecord().getCompanyId());
        System.out.println(companyInfo);
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setCompany(companyInfo.get(0).into(Company.class));
        CompanySettingsModel companySettingsModel = companyInfo.get(0).into(Tables.COMPANY_SETTINGS).into(CompanySettingsModel.class);
        companyResponse.setSettings(companySettingsModel);
        return companyResponse;
    }

    @Override
    public SuccessResponse delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        if(companyRepository.delete(employeeModel.getEmployeeRecord().getCompanyId())){
            return new SuccessResponse(true);
        } else {
            return new SuccessResponse(false);
        }
    }
}
