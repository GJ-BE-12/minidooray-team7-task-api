package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
