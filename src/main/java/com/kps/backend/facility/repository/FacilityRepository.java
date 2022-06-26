package com.kps.backend.facility.repository;

import com.example.jooq.models.tables.records.FacilityRecord;

import java.util.List;

public interface FacilityRepository {
    FacilityRecord insert(FacilityRecord record);
    FacilityRecord update(FacilityRecord record);
    FacilityRecord getById(Long facilityId);
    List<FacilityRecord> getByCompanyId(Long companyId);
    Boolean delete(Long facilityId);
    Boolean existsById(Long facilityId);
}
