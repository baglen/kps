package com.kps.backend.facility.service;

import com.kps.backend.facility.request.CreateFacilityRequest;
import com.kps.backend.facility.request.EditFacilityRequest;
import com.kps.backend.facility.response.FacilityResponse;
import com.kps.backend.response.SuccessResponse;

import java.util.List;

public interface FacilityService {
    FacilityResponse create(CreateFacilityRequest request);
    FacilityResponse edit(EditFacilityRequest request);
    SuccessResponse delete(Long facilityId);
    FacilityResponse getById(Long facilityId);
    List<FacilityResponse> getAll(Long companyId);
}
