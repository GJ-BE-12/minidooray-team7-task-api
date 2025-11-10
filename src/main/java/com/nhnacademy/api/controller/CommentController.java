package com.nhnacademy.api.controller;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.request.CommentAdd;
import com.nhnacademy.api.request.CommentUpdate;
import com.nhnacademy.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDTO> getComments(@PathVariable("projectId") long projectId,
                                        @PathVariable("taskId") long taskId){
        return commentService.getComments(projectId, taskId);
    }

    @PostMapping
    public ResponseEntity addComment(@PathVariable("projectId") long projectId,
                                     @PathVariable("taskId") long taskId,
                                     @RequestBody CommentAdd commentAdd){
        commentService.addComment(projectId, taskId, commentAdd);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity updateComment(@PathVariable("projectId") long projectId,
                                        @PathVariable("taskId") long taskId,
                                        @PathVariable("commentId") long commentId,
                                        @RequestBody CommentUpdate commentUpdate){
        commentService.updateComment(projectId, taskId, commentId, commentUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("projectId") long projectId,
                                        @PathVariable("taskId") long taskId,
                                        @PathVariable("commentId") long commentId,
                                        @RequestParam("projectMemberId") long projectMemberId){
        commentService.deleteComment(projectId, taskId, commentId, projectMemberId);
        return ResponseEntity.ok().build();
    }

}
