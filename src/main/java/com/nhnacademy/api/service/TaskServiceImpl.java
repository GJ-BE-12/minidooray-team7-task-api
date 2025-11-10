package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TaskDTO;
import com.nhnacademy.api.dto.TasksDTO;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.TaskAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public boolean exist(long taskId) {
        return taskRepository.existsTaskById(taskId);
    }

    @Override
    public boolean exist(long projectId, String title) {
        return taskRepository.existsTaskByProject_IdAndTitle(projectId, title);
    }

    @Override
    public void addTask(TaskAdd taskAdd) {
        if(!projectRepository.existsProjectById(taskAdd.getProjectId()))
            throw new RuntimeException("존하지 않는 프로젝트입니다.");
        if(exist(taskAdd.getProjectId(), taskAdd.getTitle()))
            throw new RuntimeException("중복된 제목으로 등록이 불가합니다.");

        Task newTask = new Task(taskAdd.getTitle(), taskAdd.getBody());
        taskRepository.save(newTask);
    }

    @Override
    public TaskDTO getTask(long taskId) {
        if(!exist(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");
        Task task = taskRepository.findTaskById(taskId);
        return new TaskDTO(task.getId(), task.getProject().getId(), task.getProjectMember().getId(),
                task.getTitle(), task.getBody(), task.getCreatedAt(),
                task.createCommentDTO() , task.createTagDTO(), task.createMileStoneDTO());
    }

    @Override
    public List<TasksDTO> getTasks(long projectId) {
        return taskRepository.findAllByProject_Id(projectId).stream()
                .map(t -> new TasksDTO(t.getId(), t.getProject().getId(), t.getProjectMember().getId(),
                        t.getTitle(), t.getCreatedAt()))
                .toList();
    }
}
