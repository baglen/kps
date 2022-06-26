package com.kps.backend.facility.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kps.backend.facility.model.Coordinates;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EditFacilityRequest {
    @NotNull
    @JsonProperty("facility_id")
    private Long facilityId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String address;
    private Coordinates coordinates;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Long getFacilityId() {
        return facilityId;
    }
}
