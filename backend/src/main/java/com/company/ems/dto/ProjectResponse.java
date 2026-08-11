package com.company.ems.dto;

public class ProjectResponse {
    private Long projectId;
    private String projectName;

    public ProjectResponse(Long projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }
}
