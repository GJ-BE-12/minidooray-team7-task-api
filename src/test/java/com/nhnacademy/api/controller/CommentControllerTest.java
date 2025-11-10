package com.nhnacademy.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.request.CommentAdd;
import com.nhnacademy.api.request.CommentUpdate;
import com.nhnacademy.api.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Controller가 의존하는 Service를 Mocking
    @MockitoBean
    private CommentService commentService;

    private final long PROJECT_ID = 1L;
    private final long TASK_ID = 10L;
    private final long MEMBER_ID = 100L;
    private final long COMMENT_ID = 50L;
    private final String BASE_URL = "/project/{projectId}/tasks/{taskId}/comments";

    // --------------------------------------------------------------------------
    // 1. GET /comments (댓글 조회) - 3가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-1. GET 댓글 목록 조회 성공 (데이터 존재)")
    void getComments_Success_WithData() throws Exception {
        // Given
        ZonedDateTime now = ZonedDateTime.now();
        CommentDTO mockDto = new CommentDTO(COMMENT_ID, TASK_ID, MEMBER_ID, "Test Comment", now, now);
        when(commentService.getComments(anyLong(), anyLong(), anyLong())).thenReturn(List.of(mockDto));

        // When & Then
        mockMvc.perform(get(BASE_URL, PROJECT_ID, TASK_ID)
                        .param("projectMemberId", String.valueOf(MEMBER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test Comment"));
        
        verify(commentService, times(1)).getComments(PROJECT_ID, TASK_ID, MEMBER_ID);
    }
    
    @Test
    @DisplayName("1-2. GET 댓글 목록 조회 성공 (빈 리스트 반환)")
    void getComments_Success_EmptyList() throws Exception {
        // Given
        when(commentService.getComments(anyLong(), anyLong(), anyLong())).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get(BASE_URL, PROJECT_ID, TASK_ID)
                        .param("projectMemberId", String.valueOf(MEMBER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }



    // --------------------------------------------------------------------------
    // 2. POST /comments (댓글 추가) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-3. POST 댓글 추가 성공 (200 OK)")
    void addComment_Success() throws Exception {
        // Given
        CommentAdd request = new CommentAdd(MEMBER_ID, "New Comment Content");
        doNothing().when(commentService).addComment(anyLong(), anyLong(), any(CommentAdd.class));

        // When & Then
        mockMvc.perform(post(BASE_URL, PROJECT_ID, TASK_ID)
                        .with(csrf()) // Security 설정을 위해 CSRF 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        verify(commentService, times(1)).addComment(eq(PROJECT_ID), eq(TASK_ID), any(CommentAdd.class));
    }



    // --------------------------------------------------------------------------
    // 3. PUT /comments/{commentId} (댓글 수정) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-4. PUT 댓글 수정 성공 (200 OK)")
    void updateComment_Success() throws Exception {
        // Given
        CommentUpdate request = new CommentUpdate(MEMBER_ID,"Updated Content");
        doNothing().when(commentService).updateComment(anyLong(), anyLong(), eq(COMMENT_ID), any(CommentUpdate.class));

        // When & Then
        mockMvc.perform(put(BASE_URL + "/{commentId}", PROJECT_ID, TASK_ID, COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        verify(commentService, times(1)).updateComment(eq(PROJECT_ID), eq(TASK_ID), eq(COMMENT_ID), any(CommentUpdate.class));
    }



    // --------------------------------------------------------------------------
    // 4. DELETE /comments/{commentId} (댓글 삭제) - 2가지 케이스
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("1-5. DELETE 댓글 삭제 성공 (200 OK)")
    void deleteComment_Success() throws Exception {
        // Given
        doNothing().when(commentService).deleteComment(anyLong(), anyLong(), eq(COMMENT_ID), anyLong());

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/{commentId}", PROJECT_ID, TASK_ID, COMMENT_ID)
                        .param("projectMemberId", String.valueOf(MEMBER_ID))
                        .with(csrf()))
                .andExpect(status().isOk());
        
        verify(commentService, times(1)).deleteComment(PROJECT_ID, TASK_ID, COMMENT_ID, MEMBER_ID);
    }
    

}