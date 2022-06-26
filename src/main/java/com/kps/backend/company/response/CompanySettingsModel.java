package com.kps.backend.company.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanySettingsModel {
    @JsonProperty("max_cloud_size")
    private Integer maxCloudSize;
    @JsonProperty("max_photo_count")
    private Integer maxPhotoCount;
    @JsonProperty("max_template_count")
    private Integer maxTemplateCount;

    public Integer getMaxCloudSize() {
        return maxCloudSize;
    }

    public void setMaxCloudSize(Integer maxCloudSize) {
        this.maxCloudSize = maxCloudSize;
    }

    public Integer getMaxPhotoCount() {
        return maxPhotoCount;
    }

    public void setMaxPhotoCount(Integer maxPhotoCount) {
        this.maxPhotoCount = maxPhotoCount;
    }

    public Integer getMaxTemplateCount() {
        return maxTemplateCount;
    }

    public void setMaxTemplateCount(Integer maxTemplateCount) {
        this.maxTemplateCount = maxTemplateCount;
    }
}
