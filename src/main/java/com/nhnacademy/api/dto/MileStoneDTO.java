package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class MileStoneDTO {
    private long milestoneId;
    private long taskId;
    private String name;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public MileStoneDTO(long milestoneId, long taskId, String name, ZonedDateTime createdAt){
        this.milestoneId = milestoneId;
        this.taskId = taskId;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }
}
