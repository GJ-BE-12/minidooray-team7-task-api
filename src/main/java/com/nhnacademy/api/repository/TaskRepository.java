package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsTaskById(long id);

    Task findTaskById(long id);

    boolean existsTaskByProject_IdAndTitle(long projectId, String title);

    List<Task> findAllByProject_Id(long projectId);

    void deleteTaskById(long id);

    boolean existsTaskByIdAndProjectMember_Id(long id, long projectMemberId);
}
