package com.kps.backend.task.repository;

import com.example.jooq.models.tables.records.FacilityRecord;
import com.example.jooq.models.tables.records.StatusRecord;
import com.example.jooq.models.tables.records.TaskRecord;
import com.example.jooq.models.tables.records.TaskStatusRecord;
import org.jooq.Record;
import org.jooq.Result;

public interface TaskRepository {
    TaskRecord insert(TaskRecord record);
    TaskRecord update(TaskRecord record);
    TaskRecord getById(Long taskId);
    Result<Record> getTaskStatus(Long taskId);
    Result<Record> getInfo(Long taskId);
    Result<Record> getFacilityTasks(Long facilityId);
    Result<Record> getPerformerTasks(Long performerId);
    Boolean delete(Long taskId);
    Boolean isCompleted(Long taskId);
    FacilityRecord getFacilityByTaskId(Long taskId);
    StatusRecord getStatusByName(String name);
    TaskStatusRecord addStatus(TaskStatusRecord record);
}
