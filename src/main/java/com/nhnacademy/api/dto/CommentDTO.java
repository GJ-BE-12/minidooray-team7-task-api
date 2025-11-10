package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private long commentId;
    private long taskId;
    private long projectMemberId;
    private String content;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
