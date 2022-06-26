package com.kps.backend.company.repository;

import com.example.jooq.models.tables.records.CompanyRecord;
import com.example.jooq.models.tables.records.CompanySettingsRecord;
import org.jooq.Record;
import org.jooq.Result;

public interface CompanyRepository {
    CompanyRecord insert(CompanyRecord record);
    CompanySettingsRecord insertSettings(CompanySettingsRecord record);
    CompanyRecord edit(CompanyRecord record);
    CompanySettingsRecord editSettings(CompanySettingsRecord record);
    CompanyRecord getById(Long companyId);
    Result<Record> getCompanyWithSettings(Long companyId);
    Boolean delete(Long companyId);
    Boolean existsById(Long companyId);
    Boolean existsByName(String name);
    Boolean userHasCompany(Long userId);
}
