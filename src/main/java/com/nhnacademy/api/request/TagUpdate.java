package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagUpdate {
    private long projectMemberId;
    private String name;
}
