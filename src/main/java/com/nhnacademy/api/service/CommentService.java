package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.request.CommentAdd;

import java.util.List;

public interface CommentService {
    boolean exist(long commentId);
    void addComment(CommentAdd commentAdd);
    List<CommentDTO> getComments(long taskId);
    void updateComment(long commentId, long projectMemberId, String content);
    void deleteComment(long commentId, long taskId, long projectMemberId);
}
