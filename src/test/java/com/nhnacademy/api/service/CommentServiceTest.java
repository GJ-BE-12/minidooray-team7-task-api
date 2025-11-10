package com.nhnacademy.api.service;

import com.nhnacademy.api.dto.CommentDTO;
import com.nhnacademy.api.entity.Comment;
import com.nhnacademy.api.entity.Project;
import com.nhnacademy.api.entity.ProjectMember;
import com.nhnacademy.api.entity.Task;
import com.nhnacademy.api.repository.*;
import com.nhnacademy.api.request.CommentAdd;
import com.nhnacademy.api.request.CommentUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    // ServiceImpl이 의존하는 모든 Repository Mocking
    @Mock private CommentRepository commentRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private ProjectMemberRepository projectMemberRepository;
    @Mock private ProjectRepository projectRepository;

    private final long PROJECT_ID = 1L;
    private final long TASK_ID = 10L;
    private final long COMMENT_ID = 50L;
    private final long MEMBER_ID = 100L;
    private final ZonedDateTime NOW = ZonedDateTime.now();

    // Mock 엔티티 생성 헬퍼 메서드
    private Comment createMockComment() {
        // Comment 엔티티에 setTask, setProjectMember 등을 위해 Setter가 필요함
        Comment mockComment = new Comment("Test Content");
        // Reflection을 사용하여 ID 및 시간 필드 강제 주입 (DTO 변환 테스트용)
        ReflectionTestUtils.setField(mockComment, "id", COMMENT_ID);
        ReflectionTestUtils.setField(mockComment, "createdAt", NOW);
        ReflectionTestUtils.setField(mockComment, "updatedAt", NOW);
        
        ProjectMember mockMember = Mockito.mock(ProjectMember.class);
        when(mockMember.getId()).thenReturn(MEMBER_ID);
        mockComment.setProjectMember(mockMember);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        mockComment.setTask(mockTask);
        
        return mockComment;
    }

    // --------------------------------------------------------------------------
    // A. Exist/Permission 검증 로직 테스트 (브랜치 커버리지 확보) - 총 8개 테스트
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("A1. exist(projectId, taskId): 모두 존재 시 성공")
    void exist_ProjectAndTask_Success() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsById(TASK_ID)).thenReturn(true);
        
        assertDoesNotThrow(() -> commentService.exist(PROJECT_ID, TASK_ID));
    }
    
    @Test
    @DisplayName("A2. exist(projectId, taskId): Project 미존재 시 예외 발생")
    void exist_Project_Fail() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> commentService.exist(PROJECT_ID, TASK_ID));
    }
    
    @Test
    @DisplayName("A3. exist(projectId, taskId): Task 미존재 시 예외 발생")
    void exist_Task_Fail() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsById(TASK_ID)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> commentService.exist(PROJECT_ID, TASK_ID));
    }
    
    @Test
    @DisplayName("A4. exist(projectId, taskId, commentId): Comment 미존재 시 예외 발생")
    void exist_Comment_Fail() {
        // exist(projectId, taskId) 성공 가정
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsById(TASK_ID)).thenReturn(true);
        when(commentRepository.existsCommentById(COMMENT_ID)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> commentService.exist(PROJECT_ID, TASK_ID, COMMENT_ID));
    }

    @Test
    @DisplayName("A5. isPermission(projectId, memberId): 권한 존재 시 성공")
    void isPermission_ProjectMember_Success() {
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(true);
        
        assertDoesNotThrow(() -> commentService.isPermission(PROJECT_ID, MEMBER_ID));
    }

    @Test
    @DisplayName("A6. isPermission(projectId, memberId): 권한 미존재 시 예외 발생")
    void isPermission_ProjectMember_Fail() {
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> commentService.isPermission(PROJECT_ID, MEMBER_ID));
    }

    // --------------------------------------------------------------------------
    // B. CRUD 비즈니스 로직 테스트 - 총 7개 테스트
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("B1. addComment: 댓글 추가 성공")
    void addComment_Success() {
        // Given: 검증 성공 가정
        setupSuccessExistAndPermission();
        CommentAdd commentAdd = new CommentAdd(MEMBER_ID,"New Comment");
        
        ProjectMember mockMember = Mockito.mock(ProjectMember.class);
        when(mockMember.getComments()).thenReturn(new ArrayList<>());
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.getComments()).thenReturn(new ArrayList<>());
        
        when(projectMemberRepository.findProjectMemberById(MEMBER_ID)).thenReturn(mockMember);
        when(taskRepository.findTaskById(TASK_ID)).thenReturn(mockTask);
        
        // When
        commentService.addComment(PROJECT_ID, TASK_ID, commentAdd);
        
        // Then
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertThat(mockTask.getComments()).hasSize(1);
        assertThat(mockMember.getComments()).hasSize(1);
    }
    
    @Test
    @DisplayName("B2. getComments: 댓글 목록 조회 성공 및 DTO 변환 확인")
    void getComments_Success() {
        // Given: 검증 성공 가정
        setupSuccessExistAndPermission();
        
        Comment mockComment = createMockComment();
        List<Comment> mockList = List.of(mockComment);
        when(commentRepository.findAllByTask_Id(TASK_ID)).thenReturn(mockList);

        // When
        List<CommentDTO> result = commentService.getComments(PROJECT_ID, TASK_ID, MEMBER_ID);
        
        // Then
        assertThat(result).hasSize(1);
        CommentDTO dto = result.get(0);
        assertThat(dto.getCommentId()).isEqualTo(COMMENT_ID);
        assertThat(dto.getContent()).isEqualTo("Test Content");
        // DTO 변환 시 NullPointerException이 발생하지 않는지 간접적으로 커버
    }

    @Test
    @DisplayName("B6. addComment: ProjectMember 미존재 시 예외 발생 (Service 내부 검증)")
    void addComment_Fail_ProjectMemberNotFound() {
        // Given: exist/isPermission 성공 가정
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsById(TASK_ID)).thenReturn(true);
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(true);
        
        CommentAdd commentAdd = new CommentAdd(MEMBER_ID,"New Comment");
        // findProjectMemberById가 null을 반환하거나 예외를 던질 수 있음 (Service가 처리해야 함)
        when(projectMemberRepository.findProjectMemberById(MEMBER_ID)).thenReturn(null);

        // When & Then
        // findProjectMemberById가 null을 반환할 때 NullPointerException이 발생할 가능성 (Service 로직의 안정성 검증)
        assertThrows(NullPointerException.class, () -> commentService.addComment(PROJECT_ID, TASK_ID, commentAdd));
    }
    
    // B7. getComments: 댓글 리스트가 비어있을 때
    @Test
    @DisplayName("B7. getComments: 댓글 목록이 비어있을 때 빈 리스트 반환")
    void getComments_EmptyList() {
        // Given: 검증 성공 가정
        setupSuccessExistAndPermission();
        when(commentRepository.findAllByTask_Id(TASK_ID)).thenReturn(List.of());

        // When
        List<CommentDTO> result = commentService.getComments(PROJECT_ID, TASK_ID, MEMBER_ID);
        
        // Then
        assertThat(result).isNotNull().isEmpty();
    }


    // 모든 검증 메서드가 성공적으로 실행되도록 Mocking 설정
    private void setupSuccessExistAndPermission() {
        when(projectRepository.existsProjectById(PROJECT_ID)).thenReturn(true);
        when(taskRepository.existsById(TASK_ID)).thenReturn(true);
        when(projectMemberRepository.existsProjectMemberByIdAndProject_Id(MEMBER_ID, PROJECT_ID)).thenReturn(true);
        // 나머지 exists는 필요에 따라 호출되어야 하므로 Mocking하지 않음
    }
}