package com.kps.backend.facility.repository;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.FacilityRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacilityRepositoryImpl implements FacilityRepository{

    private final DSLContext context;

    public FacilityRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public FacilityRecord insert(FacilityRecord record) {
        return context.insertInto(Tables.FACILITY)
                .set(record)
                .returning().fetchOne();
    }

    @Override
    public FacilityRecord update(FacilityRecord record) {
        return context.update(Tables.FACILITY)
                .set(Tables.FACILITY.NAME, record.getName())
                .set(Tables.FACILITY.DESCRIPTION, record.getDescription())
                .set(Tables.FACILITY.ADDRESS, record.getAddress())
                .set(Tables.FACILITY.LATITUDE, record.getLatitude())
                .set(Tables.FACILITY.LONGITUDE, record.getLongitude())
                .where(Tables.FACILITY.ID.eq(record.getId()))
                .returning()
                .fetchOne();
    }

    @Override
    public FacilityRecord getById(Long facilityId) {
        return context.selectFrom(Tables.FACILITY)
                .where(Tables.FACILITY.ID.eq(facilityId))
                .fetchOne();
    }

    @Override
    public List<FacilityRecord> getByCompanyId(Long companyId) {
        return context.selectFrom(Tables.FACILITY)
                .where(Tables.FACILITY.COMPANY_ID.eq(companyId))
                .fetch();
    }

    @Override
    public Boolean delete(Long facilityId) {
        return context.deleteFrom(Tables.FACILITY)
                .where(Tables.FACILITY.ID.eq(facilityId))
                .execute() == 1;
    }

    @Override
    public Boolean existsById(Long facilityId) {
        return context.selectFrom(Tables.FACILITY)
                .where(Tables.FACILITY.ID.eq(facilityId))
                .execute() > 0;
    }
}
