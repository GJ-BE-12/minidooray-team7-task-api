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
@Table(name = "mile_stone")
public class MileStone {
    @Id
    @Column(name = "milestone_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(name = "task_id")
//    private long taskId;
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Length(min = 1, max = 45)
    @Column(name = "milestone_name")
    private String name;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    public MileStone(String name){
        this.name = name;
        this.createdAt = ZonedDateTime.now();
    }
}
