package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_member")
public class ProjectMember {
    @Id
    @Column(name = "project_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private String userId;

//    @Column(name = "project_id")
//    private long projectId;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "join_at")
    private ZonedDateTime joinAt;

    public ProjectMember(String userId){
        this.userId = userId;
        this.joinAt = ZonedDateTime.now();
    }

    @OneToMany(mappedBy = "projectMember")
    private List<Task> tasks;

    @OneToMany(mappedBy = "projectMember")
    private List<Comment> comments;
}
