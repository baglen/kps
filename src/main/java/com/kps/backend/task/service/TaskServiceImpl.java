package com.kps.backend.task.service;

import com.example.jooq.models.Tables;
import com.example.jooq.models.tables.records.*;
import com.kps.backend.employee.repository.EmployeeRepository;
import com.kps.backend.exception.BadRequestException;
import com.kps.backend.exception.ObjectNotFoundException;
import com.kps.backend.exception.RestrictedException;
import com.kps.backend.facility.repository.FacilityRepository;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.model.EmployeeModel;
import com.kps.backend.task.model.PerformerModel;
import com.kps.backend.task.model.StatusModel;
import com.kps.backend.task.repository.TaskRepository;
import com.kps.backend.task.request.CreateTaskRequest;
import com.kps.backend.task.request.EditTaskRequest;
import com.kps.backend.task.response.TaskResponse;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final FacilityRepository facilityRepository;
    private final ModelMapper mapper;

    public TaskServiceImpl(TaskRepository taskRepository, EmployeeRepository employeeRepository, FacilityRepository facilityRepository, ModelMapper mapper) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.facilityRepository = facilityRepository;
        this.mapper = mapper;
        this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public TaskResponse create(CreateTaskRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        Result<Record> performer = employeeRepository.getEmployeeById(request.getPerformerId());
        if(performer.isEmpty()){
            throw new ObjectNotFoundException("Requested performer not found");
        }
        if(!performer.get(0).into(Tables.ROLE).getName().equals("performer")){
            throw new BadRequestException("Requested user is not a performer");
        }
        if(!performer.get(0).into(Tables.EMPLOYEE).getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new BadRequestException("Not a company performer");
        }
        TaskRecord taskRecord = mapper.map(request, TaskRecord.class);
        taskRecord.setManagerId(employeeModel.getEmployeeRecord().getId());
        taskRecord.setCreatedDate(Instant.now().getEpochSecond());
        taskRecord = taskRepository.insert(taskRecord);
        TaskResponse taskResponse = taskRecord.into(TaskResponse.class);
        StatusRecord status = taskRepository.getStatusByName("waiting");
        TaskStatusRecord taskStatus = taskRepository.addStatus(new TaskStatusRecord()
                .setDate(Instant.now().getEpochSecond())
                .setTaskId(taskRecord.getId())
                .setStatusId(status.getId()));
        StatusModel statusModel = new StatusModel();
        statusModel.setDate(taskStatus.getDate());
        statusModel.setName(status.getName());
        taskResponse.setStatus(statusModel);
        PerformerModel performerModel = performer.get(0).into(Tables.EMPLOYEE).into(PerformerModel.class);
        taskResponse.setPerformer(performerModel);
        return taskResponse;
    }

    @Override
    public TaskResponse edit(EditTaskRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        TaskRecord taskRecord = taskRepository.getById(request.getId());
        if(taskRecord == null){
            throw new ObjectNotFoundException("Requested task not found");
        }
        FacilityRecord facility = taskRepository.getFacilityByTaskId(taskRecord.getId());
        if(!facility.getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new RestrictedException("Forbidden");
        }
        if(taskRepository.isCompleted(taskRecord.getId())){
            throw new RestrictedException("Cannot edit completed task");
        }
        taskRecord.setName(request.getName());
        taskRecord.setDescription(request.getDescription());
        taskRecord.setMinPhotoCount(request.getMinPhotoCount());
        taskRecord.setExecutionDate(request.getExecutionDate());
        taskRecord.setPerformerId(request.getPerformerId());
        taskRecord.setExecutionHours(request.getExecutionHours());
        taskRecord = taskRepository.update(taskRecord);
        Result<Record> taskInfo = taskRepository.getInfo(taskRecord.getId());
        TaskResponse taskResponse = taskInfo.get(0).into(Tables.TASK).into(TaskResponse.class);
        StatusModel statusModel = new StatusModel();
        statusModel.setDate(taskInfo.get(taskInfo.size()-1).into(Tables.TASK_STATUS).getDate());
        statusModel.setName(taskInfo.get(taskInfo.size()-1).into(Tables.STATUS).getName());
        taskResponse.setStatus(statusModel);
        PerformerModel performerModel = taskInfo.get(0).into(Tables.EMPLOYEE).into(PerformerModel.class);
        taskResponse.setPerformer(performerModel);
        return taskResponse;
    }

    @Override
    public SuccessResponse delete(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        FacilityRecord facility = taskRepository.getFacilityByTaskId(taskId);
        if(facility == null){
            throw new ObjectNotFoundException("Requested task not found");
        }
        if(!facility.getCompanyId().equals(employeeModel.getEmployeeRecord().getCompanyId())){
            throw new RestrictedException("Forbidden");
        }
        if(taskRepository.delete(taskId)){
            return new SuccessResponse(true);
        }
        return new SuccessResponse(false);
    }

    @Override
    public List<TaskResponse> getByFacility(Long facilityId) {
        if(!facilityRepository.existsById(facilityId)){
            throw new ObjectNotFoundException("Requested facility not found");
        }
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Record record:taskRepository.getFacilityTasks(facilityId)) {
            TaskResponse taskResponse = record.into(Tables.TASK).into(TaskResponse.class);
            StatusModel statusModel = taskRepository.getTaskStatus(record.into(Tables.TASK).getId()).get(0).into(StatusModel.class);
            taskResponse.setStatus(statusModel);
            PerformerModel performerModel = record.into(Tables.EMPLOYEE).into(PerformerModel.class);
            taskResponse.setPerformer(performerModel);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public List<TaskResponse> getPerformersTask(Long performerId) {
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Record record:taskRepository.getPerformerTasks(performerId)) {
            TaskResponse taskResponse = record.into(Tables.TASK).into(TaskResponse.class);
            StatusModel statusModel = taskRepository.getTaskStatus(record.into(Tables.TASK).getId()).get(0).into(StatusModel.class);
            taskResponse.setStatus(statusModel);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }
}
