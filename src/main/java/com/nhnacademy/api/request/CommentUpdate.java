package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentUpdate {
    private long projectMemberId;
    private String content;
}
