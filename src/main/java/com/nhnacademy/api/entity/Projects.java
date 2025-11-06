package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "Projects")
public class Projects {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 1, max = 60)
    private String name;
    private Status status;
    private long userId;
    private ZonedDateTime created_at;
}
