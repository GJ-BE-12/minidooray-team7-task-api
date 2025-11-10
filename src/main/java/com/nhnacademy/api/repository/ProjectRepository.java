package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsProjectById(long id);

    Project findProjectById(long id);

    List<Project> findAllByUserId(String userId);
}
