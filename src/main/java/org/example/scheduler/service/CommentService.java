package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.entity.Comment;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.example.scheduler.repository.CommentRepository;
import org.example.scheduler.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스
 * - 댓글 생성 기능 제공
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 댓글 저장
     *
     * @param commentRequestDto 댓글 생성 요청 데이터
     * @return 생성된 댓글 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto, Long scheduleId){
        validateScheduleExists(scheduleId);
        validateCommentLimit(scheduleId);
        Comment comment = new Comment(commentRequestDto.getContent(), scheduleId);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    /**
     * 유효한 일정 ID 검증
     * @throws ResponseStatusException 유효하지 않은 경우 404 반환
     */
    private void validateScheduleExists(Long scheduleId) {
        if(!scheduleRepository.existsById(scheduleId)){
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
    }

    /**
     * 댓글 개수 검증
     * - 단일 일정에 댓글 최대 10개
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateCommentLimit(Long scheduleId) {
        if (commentRepository.countByScheduleId(scheduleId) >= 10) {
            throw new CustomException(ErrorCode.COMMENT_LIMIT_EXCEED);
        }
    }
}
