package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.entity.MileStone;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.MileStoneRepository;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.MileStoneAdd;
import com.nhnacademy.api.request.MileStoneUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class MileStoneServiceImpl implements MileStoneService{

    private final MileStoneRepository mileStoneRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void exist(long projectId, long taskId){
        if(!projectRepository.existsProjectById(projectId))
            throw new RuntimeException("존재하지 않는 project입니다.");
        if(!taskRepository.existsTaskById(taskId))
            throw new RuntimeException("존재하지 않는 task입니다.");
    }

    @Override
    public void exist2(long projectId, long taskId) {
        exist(projectId, taskId);
        if(mileStoneRepository.existsMileStoneByTask_Id(taskId))
            throw new RuntimeException("이미 milestone이 존재합니다");
    }

    @Override
    public void exist3(long projectId, long taskId) {
        exist(projectId, taskId);
        if(!mileStoneRepository.existsMileStonesByTask_Id(taskId))
            throw new RuntimeException("존재하지 않는 milestone입니다.");
    }

    @Override
    public void exist3(long projectId, long taskId, long milestoneId) {
        exist(projectId, taskId);
        if(!mileStoneRepository.existsMileStonesById(milestoneId))
            throw new RuntimeException("존재하지 않는 milestone입니다.");
    }

    @Override
    public void isPermission(long projectId, long projectMemberId) {
        if(projectMemberRepository.existsProjectMemberByIdAndProject_Id(projectMemberId,projectId))
            throw new RuntimeException("접근권한이 없습니다.");
    }

    @Override
    @Transactional
    public void addMileStone(long projectId, long taskId, MileStoneAdd mileStoneAdd) {
        exist(projectId, taskId);

        isPermission(projectId, mileStoneAdd.getProjectMemberId());

        MileStone newMileStone = new MileStone(mileStoneAdd.getName());
        Task task = taskRepository.findTaskById(taskId);
        task.setMileStone(newMileStone);
        newMileStone.setTask(task);
        mileStoneRepository.save(newMileStone);
    }

    @Override
    @Transactional
    public MileStoneDTO getMileStone(long projectId, long taskId, long projectMemberId) {
        exist2(projectId, taskId);

        isPermission(projectId, projectMemberId);

        MileStone mileStone = mileStoneRepository.findMileStoneByTask_Id(taskId);
        return new MileStoneDTO(mileStone.getId(), mileStone.getTask().getId(), mileStone.getName(),
                mileStone.getCreatedAt(), mileStone.getUpdateAt());
    }

    @Override
    @Transactional
    public void updateMileStone(long projectId, long taskId, long milestoneId, MileStoneUpdate mileStoneUpdate) {
        exist3(projectId, taskId, milestoneId);

        isPermission(projectId, mileStoneUpdate.getProjectMemberId());

        MileStone mileStone = mileStoneRepository.findMileStoneById(milestoneId);
        mileStone.setName(mileStoneUpdate.getName());
        mileStone.setUpdateAt(ZonedDateTime.now());
    }

    @Override
    @Transactional
    public void deleteMileStone(long projectId, long taskId, long milestoneId, long projectMemberId) {
        exist3(projectId, taskId, milestoneId);

        isPermission(projectId, projectMemberId);

        Task task = taskRepository.findTaskById(taskId);
        task.setMileStone(null);
        mileStoneRepository.deleteMileStoneById(milestoneId);
    }
}
