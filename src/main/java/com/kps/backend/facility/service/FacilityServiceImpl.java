package com.kps.backend.facility.service;

import com.example.jooq.models.tables.records.FacilityRecord;
import com.kps.backend.exception.ObjectNotFoundException;
import com.kps.backend.exception.RestrictedException;
import com.kps.backend.facility.model.Coordinates;
import com.kps.backend.facility.repository.FacilityRepository;
import com.kps.backend.facility.request.CreateFacilityRequest;
import com.kps.backend.facility.request.EditFacilityRequest;
import com.kps.backend.facility.response.FacilityResponse;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.model.EmployeeModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FacilityServiceImpl implements FacilityService{

    private final FacilityRepository facilityRepository;
    private final ModelMapper mapper;

    public FacilityServiceImpl(FacilityRepository facilityRepository, ModelMapper mapper) {
        this.facilityRepository = facilityRepository;
        this.mapper = mapper;
    }

    @Override
    public FacilityResponse create(CreateFacilityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        FacilityRecord facilityRecord = mapper.map(request, FacilityRecord.class);
        facilityRecord.setCompanyId(employeeModel.getEmployeeRecord().getCompanyId());
        facilityRecord.setLatitude(request.getCoordinates().getLatitude());
        facilityRecord.setLongitude(request.getCoordinates().getLongitude());
        facilityRecord = facilityRepository.insert(facilityRecord);
        FacilityResponse facilityResponse = facilityRecord.into(FacilityResponse.class);
        facilityResponse.setCoordinates(request.getCoordinates());
        return facilityResponse;
    }

    @Override
    public FacilityResponse edit(EditFacilityRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        if(!facilityRepository.existsById(request.getFacilityId())){
            throw new ObjectNotFoundException("Requested facility not found");
        }
        FacilityRecord facilityRecord = facilityRepository.getById(request.getFacilityId());
        if(!facilityRecord.getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new RestrictedException("Forbidden");
        }
        facilityRecord.setName(request.getName());
        facilityRecord.setDescription(request.getDescription());
        facilityRecord.setAddress(request.getAddress());
        facilityRecord.setLatitude(request.getCoordinates().getLatitude());
        facilityRecord.setLongitude(request.getCoordinates().getLongitude());
        facilityRecord = facilityRepository.update(facilityRecord);
        FacilityResponse facilityResponse = facilityRecord.into(FacilityResponse.class);
        facilityResponse.setCoordinates(request.getCoordinates());
        return facilityResponse;
    }

    @Override
    public SuccessResponse delete(Long facilityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        FacilityRecord facilityRecord = facilityRepository.getById(facilityId);
        if(facilityRecord == null){
            throw new ObjectNotFoundException("Requested facility not found");
        }
        if(!facilityRecord.getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new RestrictedException("Forbidden");
        }
        if(facilityRepository.delete(facilityId)){
            return new SuccessResponse(true);
        }
        return new SuccessResponse(false);
    }

    @Override
    public FacilityResponse getById(Long facilityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        FacilityRecord facilityRecord = facilityRepository.getById(facilityId);
        if(facilityRecord == null){
            throw new ObjectNotFoundException("Requested facility not found");
        }
        if(!facilityRecord.getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new RestrictedException("Forbidden");
        }
        FacilityResponse facilityResponse = facilityRecord.into(FacilityResponse.class);
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(facilityRecord.getLatitude());
        coordinates.setLongitude(facilityRecord.getLongitude());
        facilityResponse.setCoordinates(coordinates);
        return facilityResponse;
    }

    @Override
    public List<FacilityResponse> getAll(Long companyId) {
        return mapper.map(facilityRepository.getByCompanyId(companyId), new TypeToken<List<FacilityResponse>>() {}.getType());
    }
}
