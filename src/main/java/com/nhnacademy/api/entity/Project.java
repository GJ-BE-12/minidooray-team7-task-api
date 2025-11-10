package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 1, max = 60)
    private String name;

    private Status status;
    private String userId;
    private ZonedDateTime created_at;

    public Project(String name, String userId){
        this(name, Status.ACTIVE, userId);
    }

    public Project(String name, Status status, String userId){
        this.name = name;
        this.status = status;
        this.userId = userId;
        this.created_at = ZonedDateTime.now();
    }

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;
}
