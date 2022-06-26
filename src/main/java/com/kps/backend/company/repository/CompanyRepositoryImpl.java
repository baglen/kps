package com.kps.backend.company.repository;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.CompanyRecord;
import com.example.jooq.models.tables.records.CompanySettingsRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CompanyRepositoryImpl implements CompanyRepository{

    private final DSLContext context;

    public CompanyRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public CompanyRecord insert(CompanyRecord record) {
        return context.insertInto(Tables.COMPANY)
                .set(record)
                .returning()
                .fetchOne();
    }

    @Override
    public CompanySettingsRecord insertSettings(CompanySettingsRecord record) {
        return context.insertInto(Tables.COMPANY_SETTINGS)
                .set(record)
                .returning()
                .fetchOne();
    }

    @Override
    public CompanyRecord edit(CompanyRecord record) {
        return context.update(Tables.COMPANY)
                .set(Tables.COMPANY.NAME, record.getName())
                .set(Tables.COMPANY.CHANGED, Instant.now().getEpochSecond())
                .where(Tables.COMPANY.ID.eq(record.getId()))
                .returning()
                .fetchOne();
    }

    @Override
    public CompanySettingsRecord editSettings(CompanySettingsRecord record) {
        return context.update(Tables.COMPANY_SETTINGS)
                .set(Tables.COMPANY_SETTINGS.MAX_CLOUD_SIZE, record.getMaxCloudSize())
                .set(Tables.COMPANY_SETTINGS.MAX_PHOTO_COUNT, record.getMaxPhotoCount())
                .set(Tables.COMPANY_SETTINGS.MAX_TEMPLATE_COUNT, record.getMaxTemplateCount())
                .where(Tables.COMPANY_SETTINGS.COMPANY_ID.eq(record.getCompanyId()))
                .returning()
                .fetchOne();
    }

    @Override
    public CompanyRecord getById(Long companyId) {
        return context.selectFrom(Tables.COMPANY)
                .where(Tables.COMPANY.ID.eq(companyId))
                .fetchOne();
    }

    @Override
    public Result<Record> getCompanyWithSettings(Long companyId) {
        return context.select(Tables.COMPANY.fields())
                .select(Tables.COMPANY_SETTINGS.fields())
                .from(Tables.COMPANY)
                .join(Tables.COMPANY_SETTINGS).on(Tables.COMPANY_SETTINGS.COMPANY_ID.eq(Tables.COMPANY.ID))
                .where(Tables.COMPANY.ID.eq(companyId))
                .fetch();
    }

    @Override
    public Boolean delete(Long companyId) {
        return context.deleteFrom(Tables.COMPANY)
                .where(Tables.COMPANY.ID.eq(companyId))
                .execute() == 1;
    }

    @Override
    public Boolean existsById(Long companyId) {
        return context.selectFrom(Tables.COMPANY)
                .where(Tables.COMPANY.ID.eq(companyId))
                .execute() > 0;
    }

    @Override
    public Boolean existsByName(String name) {
        return context.selectFrom(Tables.COMPANY)
                .where(Tables.COMPANY.NAME.eq(name))
                .execute() > 0;
    }

    @Override
    public Boolean userHasCompany(Long userId) {
        return context.select(Tables.COMPANY.fields())
                .select(Tables.EMPLOYEE.fields())
                .select(Tables.ROLE.fields())
                .from(Tables.COMPANY)
                .join(Tables.EMPLOYEE).on(Tables.EMPLOYEE.COMPANY_ID.eq(Tables.COMPANY.ID))
                .join(Tables.ROLE).on(Tables.EMPLOYEE.ROLE_ID.eq(Tables.ROLE.ID))
                .where(Tables.EMPLOYEE.ID.eq(userId))
                .and(Tables.ROLE.NAME.eq("admin"))
                .execute() > 0;
    }
}
