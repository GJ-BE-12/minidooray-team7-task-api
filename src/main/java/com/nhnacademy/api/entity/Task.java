package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "project_member_id")
    private ProjectMember projectMember;

    @Length(min = 1, max = 80)
    private String title;

    @Length(min = 1, max = 500)
    private String body;

    public Task(String title){
        this(title, "");
    }

    public Task(String title, String body){
        this.title = title;
        this.body = body;
    }

    @OneToOne(mappedBy = "task")
    private MileStone mileStone;

    @OneToMany(mappedBy = "task")
    private List<Tag> tags;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}
