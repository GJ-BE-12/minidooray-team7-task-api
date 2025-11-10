package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class TasksDTO {
    private long taskId;
    private long projectId;
    private long projectMemberId;
    private String title;
    private ZonedDateTime createdAt;
}
