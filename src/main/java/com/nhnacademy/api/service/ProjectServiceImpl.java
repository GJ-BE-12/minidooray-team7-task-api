package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.ProjectDTO;
import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.request.ProjectAdd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public boolean exist(long id) {
        return projectRepository.existsProjectById(id);
    }

    @Override
    public void isPermission(long id, String userId) {
        if(!projectMemberRepository.existsProjectMemberByProject_IdAndUserId(id, userId))
            throw new RuntimeException("권한없는 접근");
    }

    @Override
    @Transactional
    public void addProject(ProjectAdd projectAdd) {
        Project newProject = new Project(projectAdd.getName(), projectAdd.getStatus(), projectAdd.getUserId());

        log.info("새로운 프로젝트 등록 - ID:{}, Name:{}", newProject.getId(), newProject.getName());

        projectRepository.save(newProject);
        projectRepository.flush();

        ProjectMember projectMember = new ProjectMember(projectAdd.getUserId());
        projectMember.setProject(newProject);
        projectMemberRepository.save(projectMember);
    }

    @Override
    @Transactional
    public ProjectDTO getProject(long id, String userId) {
        if(!exist(id))
            throw new RuntimeException("존재하지 않는 프로젝트 ID입니다.");

        Project project = projectRepository.findProjectById(id);
        return new ProjectDTO(project.getId(),project.getName(),project.getStatus(), project.getUserId(), project.getCreated_at());
    }

    @Override
    public List<ProjectDTO> getProjects(String userId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByUserId(userId);
        List<Project> projects = projectMembers.stream().map(ProjectMember::getProject).toList();
        return projects.stream()
                .map(p -> new ProjectDTO(p.getId(), p.getName(), p.getStatus(), p.getUserId(), p.getCreated_at()))
                .toList();
    }
}
