package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.request.CommentAdd;
import com.nhnacademy.api.request.CommentUpdate;

import java.util.List;

public interface CommentService {
    void exist(long projectId, long taskId);
    void exist(long projectId, long taskId, long commentId);
    void isPermission(long projectId, long projectMemberId);
    void isPermission(long projectId, long taskId, long commentId, long projectMemberId);
    void addComment(long projectId,long taskId, CommentAdd commentAdd);
    List<CommentDTO> getComments(long projectId,long taskId);
    void updateComment(long projectId,long taskId, long commentId, CommentUpdate commentUpdate);
    void deleteComment(long projectId,long taskId, long commentId, long projectMemberId);
}
