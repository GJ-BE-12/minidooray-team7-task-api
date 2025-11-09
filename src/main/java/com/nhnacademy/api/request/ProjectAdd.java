package com.nhnacademy.api.request;

import com.nhnacademy.api.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectAdd {
    long userId;
    String name;
    Status status;
}
