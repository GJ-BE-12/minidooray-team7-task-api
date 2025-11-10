package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TaskDTO;
import com.nhnacademy.api.dto.TasksDTO;
import com.nhnacademy.api.request.TaskAdd;

import java.util.List;

public interface TaskService {
    void exist(long projectId, String title);
    void exist(long projectId, long taskId);
    void isPermission(long projectId, long projectMemberId);
    void isPermission(long projectId, long taskId, long projectMemberId);
    void addTask(TaskAdd taskAdd);
    TaskDTO getTask(long projectId, long taskId);
    List<TasksDTO> getTasks(long projectId);
    void updateTask(long projectId, long taskId, long projectMemberId, String title, String body);
    void deleteTask(long projectId, long taskId, long projectMemberId);
}
