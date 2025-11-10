package com.nhnacademy.api.controller;

import com.nhnacademy.api.dto.ProjectMemberDTO;
import com.nhnacademy.api.request.ProjectMemberAdd;
import com.nhnacademy.api.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping("/{projectId}/members")
    public ResponseEntity addProjectMember(@PathVariable("projectId") long projectId,
                                           @RequestBody ProjectMemberAdd projectMemberAdd){
        projectMemberService.addProjectMember(projectId, projectMemberAdd.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/members")
    public List<ProjectMemberDTO> getProjectMember(@PathVariable("projectId") long projectId){
        return projectMemberService.getProjectMembers(projectId);
    }

}
