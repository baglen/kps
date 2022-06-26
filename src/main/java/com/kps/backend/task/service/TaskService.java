package com.kps.backend.task.service;

import com.kps.backend.response.SuccessResponse;
import com.kps.backend.task.request.CreateTaskRequest;
import com.kps.backend.task.request.EditTaskRequest;
import com.kps.backend.task.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse create(CreateTaskRequest request);
    TaskResponse edit(EditTaskRequest request);
    SuccessResponse delete(Long taskId);
    List<TaskResponse> getByFacility(Long facilityId);
    List<TaskResponse> getPerformersTask(Long performerId);
}
