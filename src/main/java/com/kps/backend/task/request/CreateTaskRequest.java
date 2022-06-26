package com.kps.backend.task.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateTaskRequest {
    @NotNull
    private String name;
    private String description;
    @NotNull
    @JsonProperty("facility_id")
    private Long facilityId;
    @NotNull
    @JsonProperty("min_photo_count")
    private Long minPhotoCount;
    @NotNull
    @JsonProperty("performer_id")
    private Long performerId;
    @NotNull
    @JsonProperty("execution_date")
    private Long executionDate;
    @NotNull
    @JsonProperty("execution_hours")
    private Integer executionHours;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public Long getMinPhotoCount() {
        return minPhotoCount;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public Long getExecutionDate() {
        return executionDate;
    }

    public Integer getExecutionHours() {
        return executionHours;
    }
}
