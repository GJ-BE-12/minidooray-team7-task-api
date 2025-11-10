package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentAdd {
    private long taskId;
    private long projectMemberId;
    private String content;
}
