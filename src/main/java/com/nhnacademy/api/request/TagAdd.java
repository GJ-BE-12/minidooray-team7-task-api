package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagAdd {
    private long taskId;
    private String name;
}
