package com.kps.backend.facility.request;

import com.kps.backend.facility.model.Coordinates;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateFacilityRequest {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String address;
    @NotNull
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
}
