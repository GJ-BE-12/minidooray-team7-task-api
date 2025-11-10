package com.nhnacademy.api.service;

import com.nhnacademy.api.request.TagAdd;

import java.util.List;

public interface TagService {
    boolean exist(long taskId);
    boolean exist(long taskId, String name);
    void addTag(TagAdd tagAdd);
    List getTags(long taskId);
    void updateTag(long tagId, String name);
    void deleteTag(long tagId, long taskId);
}
