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
    public boolean exist(long projectMemberId) {
        return projectMemberRepository.existsProjectMemberById(projectMemberId);
    }

    @Override
    public boolean exist(long projectMemberId, String userId) {
        return projectMemberRepository.existsProjectMemberByProject_IdAndUserId(projectMemberId, userId);
    }

    @Override
    @Transactional
    public void addProjectMember(long projectId, String userId) {
        if(!exist(projectId))
            throw new RuntimeException("존재하지 않는 프로젝트 입니다.");
        if(exist(projectId, userId))
            throw new RuntimeException("이미 존재하는 프로젝트 멤버입니다.");

        ProjectMember projectMember = new ProjectMember(userId);
        Project project = projectRepository.findProjectById(projectId);
        project.getProjectMembers().add(projectMember);
        projectMember.setProject(project);

        projectMemberRepository.save(projectMember);
    }

    @Override
    @Transactional
    public List<ProjectMemberDTO> getProjectMembers(long projectId) {
        if(!projectRepository.existsProjectById(projectId))
            throw new RuntimeException("존재하지 않은 프로젝트입니다.");
        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject_Id(projectId);
        log.info("{}",projectMembers);
        return projectMembers.stream()
                .map(pm -> new ProjectMemberDTO(pm.getId(), pm.getUserId(),
                        pm.getProject().getId(), pm.getJoinAt()))
                .toList();
    }

    @Override
    @Transactional
    public ProjectMember getProjectMember(long projectMemberId) {
        if(!exist(projectMemberId))
            throw new RuntimeException("존재하지 않은 프로젝트멤버입니다.");

        return projectMemberRepository.findProjectMemberById(projectMemberId);
    }

    @Override
    @Transactional
    public void deleteProjectMember(long projectId, String userId) {
        if(!exist(projectId))
            throw new RuntimeException("존재하지 않은 프로젝트멤버입니다.");

        Project project = projectRepository.findProjectById(projectId);
        List<ProjectMember> projectMembers = project.getProjectMembers();

        projectMembers.removeIf(m
                -> m.getProject().getId() == projectId && m.getUserId().equals(userId));
        projectMemberRepository.deleteProjectMemberByProject_IdAndUserId(projectId, userId);
    }
}
