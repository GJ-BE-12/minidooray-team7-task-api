package com.nhnacademy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagDTO {
    private long tagId;
    private long taskId;
    private String name;
}
