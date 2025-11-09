package com.nhnacademy.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(name = "task_id")
//    private long taskId;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Length(min = 1, max = 45)
    private String name;

    public Tag(String name){
        this.name = name;
    }
}
