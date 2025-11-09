package com.nhnacademy.api.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

//    @Column(name = "task_id")
//    private long taskId;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

//    @Column(name = "project_member_id")
//    private long projectMemberId;
    @ManyToOne
    @JoinColumn(name = "project_member_id")
    private ProjectMember projectMember;

    @Length(min = 1, max = 500)
    private String content;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "upated_at")
    private ZonedDateTime updatedAt;

    public Comment(String content){
        this.content = content;
        this.createdAt = ZonedDateTime.now();
    }
}
