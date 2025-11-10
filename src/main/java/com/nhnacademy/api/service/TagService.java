package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TagDTO;
import com.nhnacademy.api.request.TagAdd;
import com.nhnacademy.api.request.TagUpdate;

import java.util.List;

public interface TagService {
    void exist(long projectId, long taskId);
    void exist(long projectId, long taskId, String name);
    void exist(long projectId, long taskId, long tagId);
    void isPermission(long projectId, long projectMemberId);
    void addTag(long projectId, long taskId, TagAdd tagAdd);
    List<TagDTO> getTags(long projectId, long taskId, long projectMemberId);
    void updateTag(long projectId, long taskId, long tagId, TagUpdate tagUpdate);
    void deleteTag(long projectId, long taskId, long tagId, long projectMemberId);
}
