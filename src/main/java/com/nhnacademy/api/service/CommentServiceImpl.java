package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.entity.Comment;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.CommentRepository;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.CommentAdd;
import com.nhnacademy.api.request.CommentUpdate;
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
    private final ProjectRepository projectRepository;

    @Override
    public void exist(long projectId, long taskId) {
        if(!projectRepository.existsProjectById(projectId))
            throw new RuntimeException("존재하지 않는 project입니다.");
        if(taskRepository.existsById(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");
    }

    @Override
    public void exist(long projectId, long taskId, long commentId) {
        exist(projectId, taskId);
        if(commentRepository.existsCommentById(commentId))
            throw new RuntimeException("존재하지 않는 comment입니다.");
    }

    @Override
    public void isPermission(long projectId, long projectMemberId) {
        if(projectMemberRepository.existsProjectMemberByIdAndProject_Id(projectMemberId,projectId))
            throw new RuntimeException("접근권한이 없습니다.");
    }

    @Override
    public void isPermission(long projectId, long taskId, long commentId, long projectMemberId) {
        isPermission(projectId, taskId);
        if(commentRepository.existsCommentByIdAndProjectMember_Id(commentId, projectMemberId))
            throw new RuntimeException("권한이 없는 접근입니다.");
    }

    @Override
    public void addComment(long projectId, long taskId, CommentAdd commentAdd) {
        exist(projectId, taskId);

        isPermission(projectId, commentAdd.getProjectMemberId());

        ProjectMember projectMember = projectMemberRepository.findProjectMemberById(commentAdd.getProjectMemberId());
        Task task = taskRepository.findTaskById(taskId);

        Comment newComment = new Comment(commentAdd.getContent());
        newComment.setTask(task);
        newComment.setProjectMember(projectMember);
        task.getComments().add(newComment);
        projectMember.getComments().add(newComment);

        commentRepository.save(newComment);
    }

    @Override
    public List<CommentDTO> getComments(long projectId, long taskId) {
        exist(projectId, taskId);

        List<Comment> comments = commentRepository.findAllByTask_Id(taskId);
        return comments.stream()
                .map(c -> new CommentDTO(c.getId(), c.getTask().getId(), c.getProjectMember().getId(),
                        c.getContent(), c.getCreatedAt(), c.getUpdatedAt()))
                .toList();
    }

    @Override
    public void updateComment(long projectId, long taskId, long commentId, CommentUpdate commentUpdate) {
        exist(projectId, taskId, commentId);

        isPermission(projectId, taskId, commentId ,commentUpdate.getProjectMemberId());

        Comment comment = commentRepository.findCommentById(commentId);
        comment.setContent(commentUpdate.getContent());
        comment.setUpdatedAt(ZonedDateTime.now());
    }

    @Override
    public void deleteComment(long projectId, long taskId, long commentId, long projectMemberId) {
        exist(projectId, taskId, commentId);

        isPermission(projectId, taskId, commentId, projectMemberId);

        Task task = taskRepository.findTaskById(taskId);
        task.getComments().removeIf(c -> c.getId() == projectId);

        commentRepository.deleteCommentById(projectId);
    }
}
