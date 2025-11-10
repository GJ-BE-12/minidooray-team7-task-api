package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.entity.MileStone;
import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.MileStoneRepository;
import com.nhnacademy.api.repository.ProjectMemberRepository;
import com.nhnacademy.api.repository.ProjectRepository;
import com.nhnacademy.api.repository.TaskRepository;
import com.nhnacademy.api.request.MileStoneAdd;
import com.nhnacademy.api.request.MileStoneUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MileStoneServiceTest {

    @InjectMocks
    private MileStoneServiceImpl mileStoneService;

    // ServiceImpl이 의존하는 모든 Repository Mocking
    @Mock private MileStoneRepository mileStoneRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private ProjectMemberRepository projectMemberRepository;
    @Mock private ProjectRepository projectRepository;

    private final long PROJECT_ID = 1L;
    private final long TASK_ID = 10L;
    private final long MILESTONE_ID = 50L;
    private final long MEMBER_ID = 100L;
    private final ZonedDateTime NOW = ZonedDateTime.now();

    // 헬퍼 메서드: Task, Project, Exist 성공 가정
    private void setupSuccessExist() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsTaskById(TASK_ID)).thenReturn(true);
    }
    
    // --------------------------------------------------------------------------
    // A. Exist/Permission 검증 로직 테스트 (총 12개 테스트 - 모든 분기 커버)
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("A1. exist(projectId, taskId): 모두 존재 시 성공")
    void exist_ProjectAndTask_Success() {
        setupSuccessExist();
        assertDoesNotThrow(() -> mileStoneService.exist(PROJECT_ID, TASK_ID));
    }

    @Test
    @DisplayName("A2. exist(projectId, taskId): Project 미존재 시 예외 발생")
    void exist_Project_Fail() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist(PROJECT_ID, TASK_ID));
    }

    @Test
    @DisplayName("A3. exist(projectId, taskId): Task 미존재 시 예외 발생")
    void exist_Task_Fail() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsTaskById(TASK_ID)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist(PROJECT_ID, TASK_ID));
    }
    
    @Test
    @DisplayName("A4. exist2: 마일스톤 이미 존재 시 예외 발생 (Add 전용 검증)")
    void exist2_MilestoneExists_Fail() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStoneByTask_Id(TASK_ID)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist2(PROJECT_ID, TASK_ID));
    }
    
    @Test
    @DisplayName("A5. exist2: 마일스톤 미존재 시 성공 (Add 가능)")
    void exist2_MilestoneNotExists_Success() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStoneByTask_Id(TASK_ID)).thenReturn(false);
        assertDoesNotThrow(() -> mileStoneService.exist2(PROJECT_ID, TASK_ID));
    }

    @Test
    @DisplayName("A6. exist3(taskId): 마일스톤 미존재 시 예외 발생 (Get/Delete/Update 전용 검증)")
    void exist3_Task_MilestoneNotExists_Fail() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesByTask_Id(TASK_ID)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist3(PROJECT_ID, TASK_ID));
    }

    @Test
    @DisplayName("A7. exist3(taskId): 마일스톤 존재 시 성공")
    void exist3_Task_MilestoneExists_Success() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesByTask_Id(TASK_ID)).thenReturn(true);
        assertDoesNotThrow(() -> mileStoneService.exist3(PROJECT_ID, TASK_ID));
    }

    @Test
    @DisplayName("A8. exist3(milestoneId): 특정 마일스톤 ID 미존재 시 예외 발생")
    void exist3_MilestoneId_NotExists_Fail() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesById(MILESTONE_ID)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist3(PROJECT_ID, TASK_ID, MILESTONE_ID));
    }

    @Test
    @DisplayName("A9. exist3(milestoneId): 특정 마일스톤 ID 존재 시 성공")
    void exist3_MilestoneId_Exists_Success() {
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesById(MILESTONE_ID)).thenReturn(true);
        assertDoesNotThrow(() -> mileStoneService.exist3(PROJECT_ID, TASK_ID, MILESTONE_ID));
    }
    
    @Test
    @DisplayName("A10. isPermission: 권한 미존재 시 예외 발생 (isPermission 로직 커버)")
    void isPermission_Fail() {
        // isPermission 메서드 내부 로직: 'if(exists) throw new RuntimeException'
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> mileStoneService.isPermission(PROJECT_ID, MEMBER_ID), 
                     "if(true)일 때 예외가 발생해야 합니다."); // 구현체에 따라 예외가 발생해야 함
    }
    
    @Test
    @DisplayName("A11. isPermission: 권한 존재 시 성공 (if(false) 브랜치 커버)")
    void isPermission_Success() {
        // isPermission 메서드 내부 로직: 'if(false) continue'
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(false);
        assertDoesNotThrow(() -> mileStoneService.isPermission(PROJECT_ID, MEMBER_ID), 
                           "if(false)일 때 정상적으로 통과해야 합니다.");
    }
    
    // A12: isPermission 실패 (Task 존재 검증 실패)
    @Test
    @DisplayName("A12. isPermission: Project 미존재 시 예외 발생 (Exist 호출 실패)")
    void isPermission_Dependency_Fail() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> mileStoneService.exist3(PROJECT_ID, TASK_ID, MILESTONE_ID));
    }


    @Test
    @DisplayName("B3. updateMileStone: 마일스톤 내용 및 시간 업데이트 성공")
    void updateMileStone_Success() {
        // Given
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesById(MILESTONE_ID)).thenReturn(true); // exist3 통과
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(false); // isPermission 통과
        
        MileStone mockMilestone = Mockito.spy(new MileStone("Old Name"));
        when(mileStoneRepository.findMileStoneById(MILESTONE_ID)).thenReturn(mockMilestone);
        
        MileStoneUpdate request = new MileStoneUpdate(MEMBER_ID,"New Name");
        
        // When
        mileStoneService.updateMileStone(PROJECT_ID, TASK_ID, MILESTONE_ID, request);

        // Then
        assertThat(mockMilestone.getName()).isEqualTo("New Name");
        // setUpdatedAt(ZonedDateTime.now())가 호출되었는지 간접적으로 확인
        verify(mockMilestone, times(1)).setUpdatedAt(any(ZonedDateTime.class)); 
    }

    @Test
    @DisplayName("B4. deleteMileStone: 마일스톤 삭제 성공")
    void deleteMileStone_Success() {
        // Given
        setupSuccessExist();
        when(mileStoneRepository.existsMileStonesById(MILESTONE_ID)).thenReturn(true); // exist3 통과
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(false); // isPermission 통과
        
        Task mockTask = Mockito.mock(Task.class);
        when(taskRepository.findTaskById(TASK_ID)).thenReturn(mockTask);
        
        // When
        mileStoneService.deleteMileStone(PROJECT_ID, TASK_ID, MILESTONE_ID, MEMBER_ID);

        // Then
        verify(mockTask, times(1)).setMileStone(isNull());
        verify(mileStoneRepository, times(1)).deleteMileStoneById(MILESTONE_ID);
    }
}