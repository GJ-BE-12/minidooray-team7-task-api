package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class TaskDTO {
    private long taskId;
    private long projectId;
    private long projectMemberId;
    private String title;
    private String body;
    private ZonedDateTime createdAt;
    private List<CommentDTO> comments;
    private List<TagDTO> tags;
    private MileStoneDTO mileStone;
}
