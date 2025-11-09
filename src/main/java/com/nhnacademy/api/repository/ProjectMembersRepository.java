package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMembersRepository extends JpaRepository<ProjectMember, Long> {
}
