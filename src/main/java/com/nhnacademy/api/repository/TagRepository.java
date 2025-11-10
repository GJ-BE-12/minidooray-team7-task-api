package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsTagByTask_IdAndName(long taskId, String name);

    List<Tag> findAllByTask_Id(long taskId);

    boolean existsTagById(long id);

    Tag findTagById(long id);

    void deleteTagById(long id);

    boolean existsTagByIdAndTask_Id(long id, long taskId);
}
