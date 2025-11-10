package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    List<ProjectMember> findAllByProject_Id(long projectId);

    boolean existsProjectMemberById(long id);

    boolean existsProjectMemberByProject_IdAndUserId(long projectId, String userId);

    ProjectMember findProjectMemberById(long id);

    void deleteProjectMemberByProject_IdAndUserId(long projectId, String userId);

    boolean existsProjectMemberByIdAndProject_Id(long id, long projectId);
}
