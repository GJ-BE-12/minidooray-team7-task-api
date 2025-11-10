package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TaskUpdate {
    private long projectMemberId;
    private String title;
    private String body;
}
