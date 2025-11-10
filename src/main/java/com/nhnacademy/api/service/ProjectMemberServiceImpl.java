package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.ProjectMemberDTO;
import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectMemberServiceImpl implements ProjectMemberService{

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void exist(long projectId) {
        if(!projectRepository.existsProjectById(projectId))
            throw new RuntimeException("존재하지 않는 프로젝트 입니다.");
    }

    @Override
    public void exist(long projectId, String userId) {
        exist(projectId);
        if(projectMemberRepository.existsByProject_IdAndUserId(projectId, userId))
            throw new RuntimeException("이미 존재하는 프로젝트 멤버입니다.");
    }

    @Override
    @Transactional
    public void addProjectMember(long projectId, String userId) {
        exist(projectId, userId);

        ProjectMember projectMember = new ProjectMember(userId);
        Project project = projectRepository.findProjectById(projectId);
        project.getProjectMembers().add(projectMember);
        projectMember.setProject(project);

        projectMemberRepository.save(projectMember);
    }

    @Override
    @Transactional
    public List<ProjectMemberDTO> getProjectMembers(long projectId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject_Id(projectId);
        log.info("{}",projectMembers);
        return projectMembers.stream()
                .map(pm -> new ProjectMemberDTO(pm.getId(), pm.getUserId(),
                        pm.getProject().getId(), pm.getJoinAt()))
                .toList();
    }

    @Override
    @Transactional
    public ProjectMember getProjectMember(long projectId, String userId) {
        exist(projectId);

        return projectMemberRepository.findProjectMemberByProject_IdAndUserId(projectId, userId);
    }

    @Override
    @Transactional
    public void deleteProjectMember(long projectId, String userId) {
        exist(projectId);

        Project project = projectRepository.findProjectById(projectId);
        List<ProjectMember> projectMembers = project.getProjectMembers();

        projectMembers.removeIf(m
                -> m.getProject().getId() == projectId && m.getUserId().equals(userId));
        projectMemberRepository.deleteProjectMemberByProject_IdAndUserId(projectId, userId);
    }
}
