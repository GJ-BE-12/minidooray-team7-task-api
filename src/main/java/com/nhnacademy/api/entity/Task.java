package com.nhnacademy.api.entity;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.dto.TagDTO;
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

    @Column(name = "craeted_at")
    private ZonedDateTime createdAt;

    public Task(String title){
        this(title, "");
    }

    public Task(String title, String body){
        this.title = title;
        this.body = body;
        this.createdAt = ZonedDateTime.now();
    }

    @OneToOne(mappedBy = "task")
    private MileStone mileStone;

    @OneToMany(mappedBy = "task")
    private List<Tag> tags;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    public List<CommentDTO> createCommentDTO(){
        return this.comments.stream()
                .map(c -> new CommentDTO(c.getId(), this.id, c.getProjectMember().getId(),
                        c.getContent(), c.getCreatedAt(), c.getUpdatedAt()))
                .toList();
    }

    public List<TagDTO> createTagDTO(){
        return this.tags.stream()
                .map(t -> new TagDTO(t.getId(), this.id, t.getName()))
                .toList();
    }

    public MileStoneDTO createMileStoneDTO(){
        return new MileStoneDTO(mileStone.getId(), this.id, mileStone.getName(),
                mileStone.getCreatedAt(), mileStone.getUpdateAt());
    }
}
