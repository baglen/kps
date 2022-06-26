package com.kps.backend.company.response;

import com.example.jooq.models.tables.pojos.Company;

public class CompanyResponse {
    private Company company;
    private CompanySettingsModel settings;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CompanySettingsModel getSettings() {
        return settings;
    }

    public void setSettings(CompanySettingsModel settings) {
        this.settings = settings;
    }
}
