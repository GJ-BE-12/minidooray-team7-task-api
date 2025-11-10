package com.nhnacademy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MileStoneUpdate {
    long projectMemberId;
    String name;
}
