package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TagDTO;
import com.nhnacademy.api.entity.Tag;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TagRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.TagAdd;
import com.nhnacademy.api.request.TagUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;
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
    public void exist(long projectId, long taskId, String name) {
        exist(projectId, taskId);
        if(tagRepository.existsTagByTask_IdAndName(taskId, name))
            throw new RuntimeException("중복된 tag입니다.");
    }

    @Override
    public void exist(long projectId, long taskId, long tagId) {
        exist(projectId, taskId);
        if(tagRepository.existsTagByIdAndTask_Id(tagId, taskId))
            throw new RuntimeException("중복된 tag입니다.");
    }

    @Override
    public void isPermission(long projectId, long projectMemberId) {
        if(projectMemberRepository.existsProjectMemberByIdAndProject_Id(projectMemberId,projectId))
            throw new RuntimeException("접근권한이 없습니다.");
    }

    @Override
    @Transactional
    public void addTag(long projectId, long taskId, TagAdd tagAdd) {
        exist(projectId, taskId, tagAdd.getName());

        isPermission(projectId, tagAdd.getProjectMemberId());

        Tag newTag = new Tag(tagAdd.getName());
        Task task = taskRepository.getReferenceById(tagAdd.getTaskId());
        newTag.setTask(task);
        task.getTags().add(newTag);

        tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public List<TagDTO> getTags(long projectId, long taskId) {
        exist(projectId, taskId);

        List<Tag> tags = tagRepository.findAllByTask_Id(taskId);

        return tags.stream()
                .map(t -> new TagDTO(t.getId(), t.getTask().getId(), t.getName()))
                .toList();
    }

    @Override
    @Transactional
    public void updateTag(long projectId, long taskId, long tagId, TagUpdate tagUpdate) {
        exist(projectId, taskId, tagId);

        isPermission(projectId, tagUpdate.getProjectMemberId());

        Tag tag = tagRepository.findTagById(tagId);
        tag.setName(tagUpdate.getName());
    }

    @Override
    @Transactional
    public void deleteTag(long projectId, long taskId, long tagId, long projectMemberId) {
        exist(projectId, taskId, tagId);

        isPermission(projectId, projectMemberId);

        Task task = taskRepository.getReferenceById(taskId);
        List<Tag> tags = task.getTags();

        tags.removeIf(t -> t.getId() == tagId);

        tagRepository.deleteTagById(tagId);
    }
}
