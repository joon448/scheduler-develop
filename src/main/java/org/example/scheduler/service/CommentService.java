package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.dto.comment.CommentUpdateRequestDto;
import org.example.scheduler.entity.Comment;
import org.example.scheduler.entity.Schedule;
import org.example.scheduler.entity.User;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.example.scheduler.repository.CommentRepository;
import org.example.scheduler.repository.ScheduleRepository;
import org.example.scheduler.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스
 * - 댓글 생성, 조회, 수정, 삭제 기능 제공
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 댓글 저장
     *
     * @param commentRequestDto 댓글 생성 요청 데이터
     * @return 생성된 댓글 응답 DTO
     */
    @Transactional
    public CommentResponseDto addCommentToSchedule(CommentRequestDto commentRequestDto, Long scheduleId, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);

        Comment comment = new Comment(commentRequestDto.getContent());
        comment.setUser(user);
        comment.setSchedule(schedule);

        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
    }

    /**
     * 특정 일정의 댓글 목록 조회
     *
     * @return 댓글 목록 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByScheduleId(Long scheduleId) {
        return commentRepository.findByScheduleIdOrderByModifiedAtDesc(scheduleId) // 최신 수정일 기준 내림차순 정렬
                .stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    /**
     * 특정 댓글 조회
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     * @return 특정 댓글 응답 DTO
     */
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long scheduleId, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CustomException(ErrorCode.COMMENT_SCHEDULE_MISMATCH);
        }

        return new CommentResponseDto(comment);
    }

    /**
     * 특정 댓글 수정
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     * @param commentUpdateRequestDto 댓글 수정 요청 데이터
     * @return 수정된 댓글 정보 응답 DTO
     */
    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, Long sessionUserId, CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CustomException(ErrorCode.COMMENT_SCHEDULE_MISMATCH);
        }
        if(!comment.getUser().getId().equals(sessionUserId)){
            throw new CustomException(ErrorCode.FORBIDDEN_NOT_OWNER, "본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        if(commentUpdateRequestDto.getContent()!=null){
            comment.updateContent(commentUpdateRequestDto.getContent());
        }

        commentRepository.flush(); // 반환 comment에 modifiedAt 반영되도록 flush
        return CommentResponseDto.from(comment);
    }

    /**
     * 특정 댓글 삭제
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     */
    @Transactional
    public void deleteComment(Long scheduleId, Long commentId, Long sessionUserId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getSchedule().getId().equals(scheduleId)){
            throw new CustomException(ErrorCode.COMMENT_SCHEDULE_MISMATCH);
        }
        if(!comment.getUser().getId().equals(sessionUserId)){
            throw new CustomException(ErrorCode.FORBIDDEN_NOT_OWNER, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }
}
