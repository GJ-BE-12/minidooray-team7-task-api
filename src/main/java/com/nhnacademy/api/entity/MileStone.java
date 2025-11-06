package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@Entity
public class MileStone {
    @Id
    @Column(name = "milestone_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "task_id")
    private long taskId;

    @Column(name = "tag_id")
    private long tagId;

    @Length(min = 1, max = 45)
    @Column(name = "milestone_name")
    private String name;

    @Column(name = "updated_at")
    private ZonedDateTime updateAt;
}
