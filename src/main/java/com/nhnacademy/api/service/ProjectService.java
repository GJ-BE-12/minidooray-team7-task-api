package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.ProjectDTO;
import com.nhnacademy.api.request.ProjectAdd;

import java.util.List;

public interface ProjectService {
    boolean exist(long id);
    void isPermission(long id, String userId);
    void addProject(ProjectAdd ProjectAdd);
    ProjectDTO getProject(long id, String userId);
    List<ProjectDTO> getProjects(String userId);
}
