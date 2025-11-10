package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TaskDTO;
import com.nhnacademy.api.dto.TasksDTO;
import com.nhnacademy.api.request.TaskAdd;

import java.util.List;

public interface TaskService {
    boolean exist(long taskId);
    boolean exist(long projectId, String title);
    void addTask(TaskAdd taskAdd);
    TaskDTO getTask(long taskId);
    List<TasksDTO> getTasks(long projectId);
}
