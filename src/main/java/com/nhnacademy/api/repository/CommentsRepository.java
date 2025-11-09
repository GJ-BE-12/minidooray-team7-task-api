package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
