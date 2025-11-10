package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsCommentById(long id);

    List<Comment> findAllByTask_Id(long taskId);

    Comment findCommentById(long id);

    void deleteCommentById(long id);

    boolean existsCommentByIdAndProjectMember_Id(long id, long projectMemberId);
}
