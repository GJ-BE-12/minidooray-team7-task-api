package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.TaskDTO;
import com.nhnacademy.api.dto.TasksDTO;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.TaskAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public void exist(long projectId, String title) {
        if(!projectRepository.existsProjectById(projectId))
            throw new RuntimeException("존재하지 않는 project입니다.");
        if(taskRepository.existsTaskByProject_IdAndTitle(projectId, title))
            throw new RuntimeException("중복된 제목으로 등록이 불가합니다.");
    }

    @Override
    public void exist(long projectId, long taskId) {
        exist(projectId);
        if(!taskRepository.existsTaskById(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");
    }

    @Override
    public void isPermission(long projectId, long projectMemberId) {
        if(projectMemberRepository.existsProjectMemberByIdAndProject_Id(projectMemberId,projectId))
            throw new RuntimeException("접근권한이 없습니다.");
    }

    @Override
    public void isPermission(long projectId, long taskId, long projectMemberId) {
        isPermission(projectId, taskId);
        if(taskRepository.existsTaskByIdAndProjectMember_Id(taskId, projectMemberId))
            throw new RuntimeException("권한이 없는 접근입니다.");
    }

    @Override
    public void addTask(TaskAdd taskAdd) {
        exist(taskAdd.getProjectId(), taskAdd.getTitle());

        isPermission(taskAdd.getProjectMemberId(),taskAdd.getProjectId());

        Task newTask = new Task(taskAdd.getTitle(), taskAdd.getBody());
        taskRepository.save(newTask);
    }

    @Override
    public TaskDTO getTask(long projectId, long taskId) {
        exist(projectId, taskId);

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

    @Override
    public void updateTask(long projectId, long taskId, long projectMemberId, String title, String body) {
        exist(projectId, taskId);

        isPermission(projectId, taskId, projectMemberId);

        Task task = taskRepository.findTaskById(taskId);

        task.setTitle(title);
        task.setBody(body);
    }

    @Override
    public void deleteTask(long projectId, long taskId, long projectMemberId) {
        exist(projectId, taskId);

        isPermission(projectId, taskId, projectMemberId);

        taskRepository.deleteTaskById(taskId);
    }
}
