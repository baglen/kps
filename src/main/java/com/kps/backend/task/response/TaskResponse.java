package com.kps.backend.task.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kps.backend.task.model.PerformerModel;
import com.kps.backend.task.model.StatusModel;

public class TaskResponse {
    private Long id;
    @JsonProperty("facility_id")
    private Long facilityId;
    @JsonProperty("manager_id")
    private Long managerId;
    private String name;
    private String description;
    @JsonProperty("min_photo_count")
    private Integer minPhotoCount;
    private PerformerModel performer;
    @JsonProperty("execution_date")
    private Long executionDate;
    @JsonProperty("completed_date")
    private Long completedDate;
    @JsonProperty("created_date")
    private Long createdDate;
    @JsonProperty("execution_hours")
    private Integer executionHours;
    @JsonProperty("changed_date")
    private Long changedDate;
    private StatusModel status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinPhotoCount() {
        return minPhotoCount;
    }

    public void setMinPhotoCount(Integer minPhotoCount) {
        this.minPhotoCount = minPhotoCount;
    }

    public PerformerModel getPerformer() {
        return performer;
    }

    public void setPerformer(PerformerModel performer) {
        this.performer = performer;
    }

    public Long getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Long executionDate) {
        this.executionDate = executionDate;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Long completedDate) {
        this.completedDate = completedDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getExecutionHours() {
        return executionHours;
    }

    public void setExecutionHours(Integer executionHours) {
        this.executionHours = executionHours;
    }

    public Long getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Long changedDate) {
        this.changedDate = changedDate;
    }

    public StatusModel getStatus() {
        return status;
    }

    public void setStatus(StatusModel status) {
        this.status = status;
    }
}
