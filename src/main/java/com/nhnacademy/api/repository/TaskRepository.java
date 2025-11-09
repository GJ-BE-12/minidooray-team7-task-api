package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
