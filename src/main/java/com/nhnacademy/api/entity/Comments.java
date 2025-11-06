package com.nhnacademy.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@Entity
public class Comments {
    @Id
    @Column(name = "comment_id")
    private long id;

    @Column(name = "task_id")
    private long taskId;

    @Column(name = "project_member_id")
    private long projectMemberId;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Length(min = 1, max = 500)
    private String content;
}
