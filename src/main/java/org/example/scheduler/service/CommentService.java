package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.entity.Comment;
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
        validateScheduleExists(scheduleId, "등록");
        validateCommentRequest(commentRequestDto, "등록");
        validateCommentLimit(scheduleId, "등록");
        Comment comment = new Comment(commentRequestDto.getName(), commentRequestDto.getPassword(), commentRequestDto.getContent(), scheduleId);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    /**
     * 유효한 일정 ID 검증
     * @throws ResponseStatusException 유효하지 않은 경우 404 반환
     */
    private void validateScheduleExists(Long scheduleId, String action) {
        if(!scheduleRepository.existsById(scheduleId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글 "+action+" 실패: 존재하지 않는 ID입니다.");
        }
    }

    /**
     * 댓글 생성 요청 데이터 검증
     * - 작성자명, 비밀번호, 내용 필수값 확인
     * - 작성자명: 최대 20자, 내용: 최대 100자
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateCommentRequest(CommentRequestDto commentRequestDto, String action) {
        if (commentRequestDto.getName() == null || commentRequestDto.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 작성자명을 입력해주세요.");
        }
        if (commentRequestDto.getPassword() == null || commentRequestDto.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 비밀번호를 입력해주세요.");
        }
        if (commentRequestDto.getContent() == null || commentRequestDto.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 내용을 입력해주세요.");
        }
        if(commentRequestDto.getName().length() > 20){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 작성자명은 최대 20자까지 입력 가능합니다.");
        }
        if(commentRequestDto.getContent().length() > 100){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 내용은 최대 100자까지 입력 가능합니다.");
        }
    }

    /**
     * 댓글 개수 검증
     * - 단일 일정에 댓글 최대 10개
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateCommentLimit(Long scheduleId, String action) {
        if (commentRepository.countByScheduleId(scheduleId) >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 "+action+" 실패: 하나의 일정에 최대 10개의 댓글을 달 수 있습니다.");
        }
    }
}
