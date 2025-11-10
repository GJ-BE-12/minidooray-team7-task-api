package com.nhnacademy.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.api.dto.MileStoneDTO;
import com.nhnacademy.api.request.MileStoneAdd;
import com.nhnacademy.api.request.MileStoneUpdate;
import com.nhnacademy.api.service.MileStoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MileStoneController.class)
public class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Controller가 의존하는 Service를 Mocking
    @MockitoBean
    private MileStoneService mileStoneService;

    private final long PROJECT_ID = 1L;
    private final long TASK_ID = 10L;
    private final long MEMBER_ID = 100L;
    private final long MILESTONE_ID = 20L;
    private final String BASE_URL = "/project/{projectId}/tasks/{taskId}/milestone";
    
    // 🔥 DTO/Entity 생성을 위한 ZonedDateTime 인스턴스
    private final ZonedDateTime NOW = ZonedDateTime.now();

    // --------------------------------------------------------------------------
    // 1. GET /milestone (마일스톤 조회) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-1. GET 마일스톤 조회 성공 (200 OK)")
    void getMileStone_Success() throws Exception {
        // Given
        MileStoneDTO mockDto = new MileStoneDTO(MILESTONE_ID, TASK_ID, "Release v1.0", NOW, NOW);
        when(mileStoneService.getMileStone(anyLong(), anyLong(), anyLong())).thenReturn(mockDto);

        // When & Then
        mockMvc.perform(get(BASE_URL, PROJECT_ID, TASK_ID)
                        .param("projectMemberId", String.valueOf(MEMBER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId").value(MILESTONE_ID))
                .andExpect(jsonPath("$.name").value("Release v1.0"));
        
        verify(mileStoneService, times(1)).getMileStone(PROJECT_ID, TASK_ID, MEMBER_ID);
    }


    // --------------------------------------------------------------------------
    // 2. POST /milestone (마일스톤 추가) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-2. POST 마일스톤 추가 성공 (200 OK)")
    void addMileStone_Success() throws Exception {
        // Given
        MileStoneAdd request = new MileStoneAdd(MEMBER_ID, "New Feature Target");
        doNothing().when(mileStoneService).addMileStone(anyLong(), anyLong(), any(MileStoneAdd.class));

        // When & Then
        mockMvc.perform(post(BASE_URL, PROJECT_ID, TASK_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        verify(mileStoneService, times(1)).addMileStone(eq(PROJECT_ID), eq(TASK_ID), any(MileStoneAdd.class));
    }


    // --------------------------------------------------------------------------
    // 3. PUT /milestone/{milestoneId} (마일스톤 수정) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-3. PUT 마일스톤 수정 성공 (200 OK)")
    void updateMileStone_Success() throws Exception {
        // Given
        MileStoneUpdate request = new MileStoneUpdate(MEMBER_ID, "Urgent Update");
        doNothing().when(mileStoneService).updateMileStone(anyLong(), anyLong(), eq(MILESTONE_ID), any(MileStoneUpdate.class));

        // When & Then
        mockMvc.perform(put(BASE_URL + "/{milestoneId}", PROJECT_ID, TASK_ID, MILESTONE_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        verify(mileStoneService, times(1)).updateMileStone(eq(PROJECT_ID), eq(TASK_ID), eq(MILESTONE_ID), any(MileStoneUpdate.class));
    }


    // --------------------------------------------------------------------------
    // 4. DELETE /milestone/{milestoneId} (마일스톤 삭제) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-4. DELETE 마일스톤 삭제 성공 (200 OK)")
    void deleteMileStone_Success() throws Exception {
        // Given
        doNothing().when(mileStoneService).deleteMileStone(anyLong(), anyLong(), eq(MILESTONE_ID), anyLong());

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/{milestoneId}", PROJECT_ID, TASK_ID, MILESTONE_ID)
                        .param("projectMemberId", String.valueOf(MEMBER_ID))
                        .with(csrf()))
                .andExpect(status().isOk());
        
        verify(mileStoneService, times(1)).deleteMileStone(PROJECT_ID, TASK_ID, MILESTONE_ID, MEMBER_ID);
    }

}