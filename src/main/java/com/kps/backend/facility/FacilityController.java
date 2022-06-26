package com.kps.backend.facility;

import com.kps.backend.facility.request.CreateFacilityRequest;
import com.kps.backend.facility.request.EditFacilityRequest;
import com.kps.backend.facility.response.FacilityResponse;
import com.kps.backend.facility.service.FacilityService;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.model.EmployeeModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("facility")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PreAuthorize("hasAnyRole('ROLE_manager','ROLE_admin')")
    @PostMapping("create")
    public FacilityResponse create(@Valid @RequestBody CreateFacilityRequest request){
        return facilityService.create(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_manager','ROLE_admin')")
    @PostMapping("edit")
    public FacilityResponse edit(@Valid @RequestBody EditFacilityRequest request){
        return facilityService.edit(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_manager','ROLE_admin')")
    @GetMapping("delete")
    public SuccessResponse delete(@RequestParam Long id){
        return facilityService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_manager','ROLE_admin')")
    @GetMapping("{facilityId}")
    public Object get(@PathVariable(required = false) Long facilityId){
        if(facilityId != null){
            return facilityService.getById(facilityId);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        return facilityService.getAll(employeeModel.getEmployeeRecord().getCompanyId());
    }
}
