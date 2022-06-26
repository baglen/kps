package com.kps.backend.company;

import com.kps.backend.company.request.EditCompanyRequest;
import com.kps.backend.company.response.CompanyResponse;
import com.kps.backend.company.service.CompanyService;
import com.kps.backend.response.SuccessResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("edit")
    public CompanyResponse edit(@Valid @RequestBody EditCompanyRequest request){
        return companyService.edit(request);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @GetMapping("delete")
    public SuccessResponse delete(){
        return companyService.delete();
    }

    @GetMapping()
    public CompanyResponse getById(){
        return companyService.getById();
    }
}
