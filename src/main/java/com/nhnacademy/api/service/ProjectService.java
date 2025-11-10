package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.ProjectDTO;
import com.nhnacademy.api.request.ProjectAdd;

public interface ProjectService {
    boolean exist(long id);
    void addProject(ProjectAdd ProjectAdd);
    ProjectDTO getProject(long id);
}
