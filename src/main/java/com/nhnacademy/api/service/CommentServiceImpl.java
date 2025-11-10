package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.entity.Comment;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.CommentRepository;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.CommentAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public boolean exist(long commentId) {
        return commentRepository.existsCommentById(commentId);
    }

    @Override
    public void addComment(CommentAdd commentAdd) {
        if(taskRepository.existsById(commentAdd.getTaskId()))
            throw new RuntimeException("존재하지 않는 task입니다.");

        ProjectMember projectMember = projectMemberRepository.findProjectMemberById(commentAdd.getProjectMemberId());
        Task task = taskRepository.findTaskById(commentAdd.getTaskId());
        if(projectMember.getProject().getId() == task.getProject().getId())
            throw new RuntimeException("권한이 없는 요청입니다.");

        Comment newComment = new Comment(commentAdd.getContent());
        newComment.setTask(task);
        newComment.setProjectMember(projectMember);
        task.getComments().add(newComment);
        projectMember.getComments().add(newComment);

        commentRepository.save(newComment);
    }

    @Override
    public List<CommentDTO> getComments(long taskId) {
        if(taskRepository.existsById(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");

        List<Comment> comments = commentRepository.findAllByTask_Id(taskId);
        return comments.stream()
                .map(c -> new CommentDTO(c.getId(), c.getTask().getId(), c.getProjectMember().getId(),
                        c.getContent(), c.getCreatedAt(), c.getUpdatedAt()))
                .toList();
    }

    @Override
    public void updateComment(long commentId, long projectMemberId, String content) {
        if(!exist(commentId))
            throw new RuntimeException("존재하지 않는 comment입니다.");

        Comment comment = commentRepository.findCommentById(commentId);
        if(comment.getProjectMember().getId() != projectMemberId)
            throw new RuntimeException("권한이 없는 요청입니다.");

        comment.setContent(content);
        comment.setUpdatedAt(ZonedDateTime.now());
    }

    @Override
    public void deleteComment(long commentId, long taskId, long projectMemberId) {
        if(!exist(commentId))
            throw new RuntimeException("존재하지 않는 comment입니다.");

        Comment comment = commentRepository.findCommentById(commentId);
        if(comment.getProjectMember().getId() != projectMemberId)
            throw new RuntimeException("권한이 없는 요청입니다.");

        Task task = taskRepository.findTaskById(taskId);
        task.getComments().removeIf(c -> c.getId() == commentId);

        commentRepository.deleteCommentById(commentId);
    }
}
