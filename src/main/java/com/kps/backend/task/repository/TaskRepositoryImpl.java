package com.kps.backend.task.repository;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.FacilityRecord;
import com.example.jooq.models.tables.records.StatusRecord;
import com.example.jooq.models.tables.records.TaskRecord;
import com.example.jooq.models.tables.records.TaskStatusRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TaskRepositoryImpl implements TaskRepository{

    private final DSLContext context;

    public TaskRepositoryImpl(DSLContext context) {
        this.context = context;
    }

    @Override
    public TaskRecord insert(TaskRecord record) {
        return context.insertInto(Tables.TASK)
                .set(record)
                .returning().fetchOne();
    }

    @Override
    public TaskRecord update(TaskRecord record) {
        return context.update(Tables.TASK)
                .set(Tables.TASK.NAME, record.getName())
                .set(Tables.TASK.DESCRIPTION, record.getDescription())
                .set(Tables.TASK.MIN_PHOTO_COUNT, record.getMinPhotoCount())
                .set(Tables.TASK.EXECUTION_DATE, record.getExecutionDate())
                .set(Tables.TASK.PERFORMER_ID, record.getPerformerId())
                .set(Tables.TASK.EXECUTION_HOURS, record.getExecutionHours())
                .set(Tables.TASK.CHANGED_DATE, Instant.now().getEpochSecond())
                .where(Tables.TASK.ID.eq(record.getId()))
                .returning()
                .fetchOne();
    }

    @Override
    public TaskRecord getById(Long taskId) {
        return context.selectFrom(Tables.TASK)
                .where(Tables.TASK.ID.eq(taskId))
                .fetchOne();
    }

    @Override
    public Result<Record> getTaskStatus(Long taskId) {
        return context.select(Tables.TASK.fields())
                .select(Tables.TASK_STATUS.fields())
                .select(Tables.STATUS.fields())
                .from(Tables.TASK)
                .join(Tables.TASK_STATUS).on(Tables.TASK.ID.eq(Tables.TASK_STATUS.TASK_ID))
                .join(Tables.STATUS).on(Tables.TASK_STATUS.STATUS_ID.eq(Tables.STATUS.ID))
                .where(Tables.TASK.ID.eq(taskId))
                .orderBy(Tables.TASK_STATUS.DATE.desc())
                .fetch();
    }

    @Override
    public Result<Record> getInfo(Long taskId) {
        return context.select(Tables.TASK.fields())
                .select(Tables.EMPLOYEE.fields())
                .select(Tables.TASK_STATUS.fields())
                .select(Tables.STATUS.fields())
                .from(Tables.TASK)
                .join(Tables.EMPLOYEE).on(Tables.TASK.PERFORMER_ID.eq(Tables.EMPLOYEE.ID))
                .join(Tables.TASK_STATUS).on(Tables.TASK.ID.eq(Tables.TASK_STATUS.TASK_ID))
                .join(Tables.STATUS).on(Tables.TASK_STATUS.STATUS_ID.eq(Tables.STATUS.ID))
                .where(Tables.TASK.ID.eq(taskId))
                .fetch();
    }

    @Override
    public Result<Record> getFacilityTasks(Long facilityId) {
        return context.select(Tables.TASK.fields())
                .select(Tables.EMPLOYEE.fields())
                .from(Tables.TASK)
                .join(Tables.EMPLOYEE).on(Tables.TASK.PERFORMER_ID.eq(Tables.EMPLOYEE.ID))
                .where(Tables.TASK.FACILITY_ID.eq(facilityId))
                .fetch();
    }

    @Override
    public Result<Record> getPerformerTasks(Long performerId) {
        return context.select(Tables.TASK.fields())
                .from(Tables.TASK)
                .where(Tables.TASK.PERFORMER_ID.eq(performerId))
                .fetch();
    }

    @Override
    public Boolean delete(Long taskId) {
        return context.deleteFrom(Tables.TASK)
                .where(Tables.TASK.ID.eq(taskId))
                .execute() == 1;
    }

    @Override
    public Boolean isCompleted(Long taskId) {
        return context.select(Tables.TASK.fields())
                .select(Tables.TASK_STATUS.fields())
                .select(Tables.STATUS.fields())
                .from(Tables.TASK)
                .join(Tables.TASK_STATUS).on(Tables.TASK.ID.eq(Tables.TASK_STATUS.TASK_ID))
                .join(Tables.STATUS).on(Tables.TASK_STATUS.STATUS_ID.eq(Tables.STATUS.ID))
                .where(Tables.TASK.ID.eq(taskId))
                .and(Tables.STATUS.NAME.eq("completed"))
                .execute() > 0;
    }

    @Override
    public FacilityRecord getFacilityByTaskId(Long taskId) {
        return context.select(Tables.FACILITY.fields())
                .select(Tables.TASK.fields())
                .from(Tables.FACILITY)
                .join(Tables.TASK).on(Tables.FACILITY.ID.eq(Tables.TASK.FACILITY_ID))
                .where(Tables.TASK.ID.eq(taskId))
                .fetchOne().into(Tables.FACILITY);
    }

    @Override
    public StatusRecord getStatusByName(String name) {
        return context.selectFrom(Tables.STATUS)
                .where(Tables.STATUS.NAME.eq(name))
                .fetchOne();
    }

    @Override
    public TaskStatusRecord addStatus(TaskStatusRecord record) {
        return context.insertInto(Tables.TASK_STATUS)
                .set(record)
                .returning().fetchOne();
    }
}
