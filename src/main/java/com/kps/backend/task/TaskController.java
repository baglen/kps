package com.kps.backend.task;

import com.example.jooq.models.tables.records.TaskRecord;
import com.kps.backend.facility.request.EditFacilityRequest;
import com.kps.backend.security.model.EmployeeModel;
import com.kps.backend.task.request.CreateTaskRequest;
import com.kps.backend.task.request.EditTaskRequest;
import com.kps.backend.task.response.TaskResponse;
import com.kps.backend.task.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('ROLE_manager')")
    @PostMapping("create")
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest request){
        return taskService.create(request);
    }

    @PreAuthorize("hasRole('ROLE_manager')")
    @PostMapping("edit")
    public TaskResponse edit(@Valid @RequestBody EditTaskRequest request){
        return taskService.edit(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_manager','ROLE_admin')")
    @GetMapping("facility/{facilityId}")
    public List<TaskResponse> getByFacilityId(@PathVariable Long facilityId){
        return taskService.getByFacility(facilityId);
    }

    @PreAuthorize("hasRole('ROLE_performer')")
    @GetMapping("performer")
    public List<TaskResponse> getPerformerTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EmployeeModel employeeModel = (EmployeeModel) authentication.getPrincipal();
        return taskService.getPerformersTask(employeeModel.getEmployeeRecord().getId());
    }
}
