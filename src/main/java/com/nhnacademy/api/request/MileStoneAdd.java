package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MileStoneAdd {
    private long projectMemberId;
    private String name;
}
