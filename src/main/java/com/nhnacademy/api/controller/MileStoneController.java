package com.nhnacademy.api.controller;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.request.MileStoneAdd;
import com.nhnacademy.api.request.MileStoneUpdate;
import com.nhnacademy.api.service.MileStoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project/{projectId}/tasks/{taskId}/milestone")
public class MileStoneController {
    private final MileStoneService mileStoneService;

    @GetMapping
    public MileStoneDTO getMileStone(@PathVariable("projectId") long projectId,
                                     @PathVariable("taskId") long taskId,
                                     @RequestParam("projectMemberId") long projectMemberId){
        return mileStoneService.getMileStone(projectId, taskId, projectMemberId);
    }

    @PostMapping
    public ResponseEntity addMileStone(@PathVariable("projectId") long projectId,
                                       @PathVariable("taskId") long taskId,
                                       @RequestBody MileStoneAdd mileStoneAdd){
        mileStoneService.addMileStone(projectId, taskId, mileStoneAdd);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{milestoneId}")
    public ResponseEntity updateMileStone(@PathVariable("projectId") long projectId,
                                      @PathVariable("taskId") long taskId,
                                      @PathVariable("milestoneId") long milestoneId,
                                      @RequestBody MileStoneUpdate mileStoneUpdate){
        mileStoneService.updateMileStone(projectId, taskId, milestoneId, mileStoneUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{milestoneId}")
    public ResponseEntity deleteMileStone(@PathVariable("projectId") long projectId,
                                      @PathVariable("taskId") long taskId,
                                      @PathVariable("milestoneId") long milestoneId,
                                      @RequestParam("projectMemberId") long projectMemberId){
        mileStoneService.deleteMileStone(projectId,taskId,milestoneId, projectMemberId);
        return ResponseEntity.ok().build();
    }
}
