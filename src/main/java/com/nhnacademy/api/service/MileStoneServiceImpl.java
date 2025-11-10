package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.entity.MileStone;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.MileStoneRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.MileStoneAdd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class MileStoneServiceImpl implements MileStoneService{

    private final MileStoneRepository mileStoneRepository;
    private final TaskRepository taskRepository;

    @Override
    public boolean exist(long taskId) {
        return mileStoneRepository.existsMileStoneByTask_Id(taskId);
    }

    @Override
    public boolean exist2(long milestoneId) {
        return mileStoneRepository.existsMileStonesById(milestoneId);
    }

    @Override
    @Transactional
    public void addMileStone(MileStoneAdd mileStoneAdd) {
        if(!taskRepository.existsTaskById(mileStoneAdd.getTaskId()))
            throw new RuntimeException("존재하지 않는 task입니다.");
        if(exist(mileStoneAdd.getTaskId()))
            throw new RuntimeException("이미 milestone이 존재합니다");

        MileStone newMileStone = new MileStone(mileStoneAdd.getName());
        Task task = taskRepository.findTaskById(mileStoneAdd.getTaskId());
        task.setMileStone(newMileStone);
        newMileStone.setTask(task);
        mileStoneRepository.save(newMileStone);
    }

    @Override
    @Transactional
    public MileStoneDTO getMileStone(long milestoneId) {
        if(!exist2(milestoneId))
            throw new RuntimeException("존재하지 않는 milestone입니다.");

        MileStone mileStone = mileStoneRepository.findMileStoneById(milestoneId);
        return new MileStoneDTO(mileStone.getId(), mileStone.getTask().getId(), mileStone.getName(),
                mileStone.getCreatedAt(), mileStone.getUpdateAt());
    }

    @Override
    @Transactional
    public void updateMileStone(long milestoneId, String name) {
        if(!exist2(milestoneId))
            throw new RuntimeException("존재하지 않는 milestone입니다.");

        MileStone mileStone = mileStoneRepository.findMileStoneById(milestoneId);
        mileStone.setName(name);
        mileStone.setUpdateAt(ZonedDateTime.now());
    }

    @Override
    @Transactional
    public void deleteMileStone(long milestoneId, long taskId) {
        if(!exist2(milestoneId))
            throw new RuntimeException("존재하지 않는 milestone입니다.");

        Task task = taskRepository.findTaskById(taskId);
        task.setMileStone(null);
        mileStoneRepository.deleteMileStoneById(milestoneId);
    }
}
