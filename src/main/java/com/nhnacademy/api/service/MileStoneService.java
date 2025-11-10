package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.request.MileStoneAdd;
import com.nhnacademy.api.request.MileStoneUpdate;

public interface MileStoneService {
    void exist(long projectId, long taskId);
    void exist2(long projectId, long taskId);
    void exist3(long projectId, long taskId);
    void exist3(long projectId, long taskId, long milestoneId);
    void isPermission(long projectId, long projectMemberId);
    void addMileStone(long projectId, long taskId, MileStoneAdd mileStoneAdd);
    MileStoneDTO getMileStone(long projectId, long taskId, long projectMemberId);
    void updateMileStone(long projectId, long taskId, long milestoneId, MileStoneUpdate mileStoneUpdate);
    void deleteMileStone(long projectId, long taskId, long milestoneId, long projectMemberId);
}
