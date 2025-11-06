package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Tasks")
public class Tasks {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long projectId;
    private long projectMemberId;

    @Length(min = 1, max = 80)
    private String title;

    @Length(min = 1, max = 500)
    private String body;
}
