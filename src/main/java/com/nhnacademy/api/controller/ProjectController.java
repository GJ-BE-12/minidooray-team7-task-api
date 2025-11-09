package com.nhnacademy.api.controller;

import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.request.ProjectAdd;
import com.nhnacademy.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity addProject(@RequestBody ProjectAdd projectAdd){
        projectService.addProject(projectAdd);
        return ResponseEntity.ok()/*.body()*/.build();
    }

    @GetMapping("{id}")
    public Project getProject(@PathVariable("id") long id){
        return projectService.getProject(id);
    }
}
