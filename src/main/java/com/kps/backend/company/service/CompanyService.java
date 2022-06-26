package com.kps.backend.company.service;

import com.kps.backend.company.request.EditCompanyRequest;
import com.kps.backend.company.response.CompanyResponse;
import com.kps.backend.response.SuccessResponse;

public interface CompanyService {
    CompanyResponse edit(EditCompanyRequest request);
    CompanyResponse getById();
    SuccessResponse delete();

}
