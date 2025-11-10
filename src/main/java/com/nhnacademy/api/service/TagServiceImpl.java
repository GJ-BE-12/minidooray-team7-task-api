package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TagDTO;
import com.nhnacademy.api.entity.Tag;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.TagRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.TagAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;

    @Override
    public boolean exist(long taskId) {
        return taskRepository.existsTaskById(taskId);
    }

    @Override
    public boolean exist(long taskId, String name) {
        return tagRepository.existsTagByTask_IdAndName(taskId, name);
    }

    @Override
    @Transactional
    public void addTag(TagAdd tagAdd) {
        if(!exist(tagAdd.getTaskId()))
            throw new RuntimeException("존재하지 않는 task입니다.");
        if(exist(tagAdd.getTaskId(), tagAdd.getName()))
            throw new RuntimeException("이미 존재하는 태그입니다.");

        Tag newTag = new Tag(tagAdd.getName());
        Task task = taskRepository.getReferenceById(tagAdd.getTaskId());
        newTag.setTask(task);
        task.getTags().add(newTag);

        tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public List<TagDTO> getTags(long taskId) {
        if(!exist(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");

        List<Tag> tags = tagRepository.findAllByTask_Id(taskId);

        return tags.stream()
                .map(t -> new TagDTO(t.getId(), t.getTask().getId(), t.getName()))
                .toList();
    }

    @Override
    @Transactional
    public void updateTag(long tagId, String name) {
        if(tagRepository.existsTagById(tagId))
            throw new RuntimeException("존재하지 않는 tag입니다.");

        Tag tag = tagRepository.findTagById(tagId);
        tag.setName(name);
    }

    @Override
    @Transactional
    public void deleteTag(long tagId, long taskId) {
        if(tagRepository.existsTagById(tagId))
            throw new RuntimeException("존재하지 않는 tag입니다.");

        Task task = taskRepository.getReferenceById(taskId);
        List<Tag> tags = task.getTags();

        tags.removeIf(t -> t.getId() == tagId);

        tagRepository.deleteTagById(tagId);
    }
}
