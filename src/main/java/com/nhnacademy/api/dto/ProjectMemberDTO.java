package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class ProjectMemberDTO {
    private long projectMemberId;
    private String userId;
    private long projectId;
    private ZonedDateTime joinAt;
}
