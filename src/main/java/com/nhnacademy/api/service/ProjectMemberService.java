package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.ProjectMemberDTO;
import com.nhnacademy.api.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    boolean exist(long projectMemberId);
    boolean exist(long projectId, String userId);
    void addProjectMember(long projectId, String userId);
    List<ProjectMemberDTO> getProjectMembers(long projectId);
    ProjectMember getProjectMember(long ProjectMemberId);
    void deleteProjectMember(long projectId, String userId);
}
