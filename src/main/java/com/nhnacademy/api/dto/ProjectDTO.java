package com.nhnacademy.api.dto;

import com.nhnacademy.api.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class ProjectDTO {
    private long projectId;
    private String name;
    private Status status;
    private String userId;
    private ZonedDateTime createdAt;
}
