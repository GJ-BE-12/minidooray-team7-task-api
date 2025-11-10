package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskAdd {
    private long projectId;
    private long projectMemberId;
    private String title;
    private String body;
}
