package com.kps.backend.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class RegisterRequest {
    @NotNull
    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String login;
    @NotNull
    @JsonProperty("first_name")
    private String firstName;
    @NotNull
    @JsonProperty("last_name")
    private String lastName;
    @NotNull
    private String role;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("max_cloud_size")
    private Integer maxCloudSize;
    @JsonProperty("max_photo_count")
    private Integer maxPhotoCount;
    @JsonProperty("max_template_count")
    private Integer maxTemplateCount;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Integer getMaxCloudSize() {
        return maxCloudSize;
    }

    public Integer getMaxPhotoCount() {
        return maxPhotoCount;
    }

    public Integer getMaxTemplateCount() {
        return maxTemplateCount;
    }
}
