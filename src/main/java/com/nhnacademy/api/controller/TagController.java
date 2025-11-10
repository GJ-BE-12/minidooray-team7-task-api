package com.nhnacademy.api.controller;

import com.nhnacademy.api.dto.TagDTO;
import com.nhnacademy.api.request.TagAdd;
import com.nhnacademy.api.request.TagUpdate;
import com.nhnacademy.api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project/{projectId}/tasks/{taskId}/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagDTO> getTags(@PathVariable("projectId") long projectId,
                                @PathVariable("taskId") long taskId){
        return tagService.getTags(projectId, taskId);
    }

    @PostMapping
    public ResponseEntity addTag(@PathVariable("projectId") long projectId,
                                 @PathVariable("taskId") long taskId,
                                 @RequestBody TagAdd tagAdd){
        tagService.addTag(projectId, taskId, tagAdd);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tagId}")
    public ResponseEntity updateTag(@PathVariable("projectId") long projectId,
                                    @PathVariable("taskId") long taskId,
                                    @PathVariable("tagId") long tagId,
                                    @RequestBody TagUpdate tagUpdate){
        tagService.updateTag(projectId, taskId, tagId, tagUpdate);
        return  ResponseEntity.ok().build();
    }
}
