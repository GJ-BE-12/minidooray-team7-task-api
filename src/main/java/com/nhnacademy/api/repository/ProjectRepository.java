package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsProjectById(long id);

    Project findProjectById(long id);
}
