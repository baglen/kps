package com.kps.backend.facility.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kps.backend.facility.model.Coordinates;

import java.util.List;

public class FacilityResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private Coordinates coordinates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
