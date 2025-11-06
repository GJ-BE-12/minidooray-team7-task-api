package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Entity
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "task_id")
    private long taskId;

    @Length(min = 1, max = 45)
    private String tagName;
}
