package com.kps.backend.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditCompanyRequest {
    @JsonProperty("company_id")
    private Long companyId;
    private String name;
    @JsonProperty("max_cloud_size")
    private Integer maxCloudSize;
    @JsonProperty("max_photo_count")
    private Integer maxPhotoCount;
    @JsonProperty("max_template_count")
    private Integer maxTemplateCount;

    public Long getCompanyId() {
        return companyId;
    }

    public String getName() {
        return name;
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
