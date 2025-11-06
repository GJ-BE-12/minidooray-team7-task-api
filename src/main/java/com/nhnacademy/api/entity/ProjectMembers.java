package com.nhnacademy.api.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
public class ProjectMembers {
    @Id
    @Column(name = "project_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "project_id")
    private long projectId;

    @Column(name = "join_at")
    private ZonedDateTime joinAt;
}
