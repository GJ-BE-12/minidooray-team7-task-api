package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.request.MileStoneAdd;

public interface MileStoneService {
    boolean exist(long taskId);
    boolean exist2(long milestoneId);
    void addMileStone(MileStoneAdd mileStoneAdd);
    MileStoneDTO getMileStone(long milestoneId);
    void updateMileStone(long milestoneId, String name);
    void deleteMileStone(long milestoneId, long taskId);
}
