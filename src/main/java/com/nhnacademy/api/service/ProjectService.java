package com.nhnacademy.api.service;

import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.request.ProjectAdd;

public interface ProjectService {
    boolean exist(long id);
    void addProject(ProjectAdd ProjectAdd);
    Project getProject(long id);
}
