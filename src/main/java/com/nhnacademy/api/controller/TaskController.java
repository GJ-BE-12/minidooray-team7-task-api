package com.nhnacademy.api.controller;

import com.nhnacademy.api.dto.TaskDTO;
import com.nhnacademy.api.dto.TasksDTO;
import com.nhnacademy.api.request.TaskUpdate;
import com.nhnacademy.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TasksDTO> getTasks(@PathVariable("projectId") long projectId){
        return taskService.getTasks(projectId);
    }

    @GetMapping("/{taskId}")
    public TaskDTO getTask(@PathVariable("projectId") long projectId,
                           @PathVariable("taskId") long taskId){
        return taskService.getTask(projectId, taskId);
    }

    @PutMapping("{taskId}")
    public ResponseEntity updateTask(@PathVariable("projectId") long projectId,
                                     @PathVariable("taskId") long taskId,
                                     @RequestBody TaskUpdate taskUpdate){
        taskService.updateTask(projectId, taskId, taskUpdate.getProjectMemberId(),
                taskUpdate.getTitle(), taskUpdate.getBody());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity deleteTask(@PathVariable("projectId") long projectId,
                                     @PathVariable("taskId") long taskId,
                                     @RequestParam("projectMemberId") long projectMemberId){
        taskService.deleteTask(projectId, taskId, projectMemberId);
        return ResponseEntity.ok().build();
    }
}
