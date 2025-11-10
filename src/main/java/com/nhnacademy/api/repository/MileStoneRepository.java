package com.nhnacademy.api.repository;

import com.nhnacademy.api.entity.MileStone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileStoneRepository extends JpaRepository<MileStone, Long> {
    boolean existsMileStoneByTask_Id(long taskId);

    boolean existsMileStonesById(long id);

    MileStone findMileStoneById(long id);

    void deleteMileStoneById(long id);

    boolean existsMileStonesByTask_Id(long taskId);

    MileStone findMileStoneByTask_Id(long taskId);
}
