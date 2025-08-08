package org.example.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.dto.comment.CommentUpdateRequestDto;
import org.example.scheduler.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{scheduleId}/comments")
public class CommentController {
    private final CommentService commentService;

    /**
     * 특정 일정에 댓글 등록
     *
     * @param scheduleId 일정 ID
     * @param commentRequestDto 댓글 생성 요청 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long scheduleId, @Valid @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpRequest){
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(commentService.addCommentToSchedule(commentRequestDto, scheduleId, userId),  HttpStatus.CREATED);
    }

    /**
     * 특정 일정의 댓글 목록 조회
     *
     * @param scheduleId 일정 ID
     * @return 댓글 목록 (최신 수정일 기준 정렬)
     */
    @GetMapping("/")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long scheduleId) {
        return new ResponseEntity<>(commentService.getCommentsByScheduleId(scheduleId),  HttpStatus.OK);
    }

    /**
     * 특정 ID의 댓글 조회
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     * @return 댓글
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long scheduleId, @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(scheduleId, commentId), HttpStatus.OK);
    }

    /**
     * 특정 ID의 댓글 수정
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     * @param commentUpdateRequestDto 댓글 수정 요청 정보
     * @return 수정된 댓글 정보
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long scheduleId, @PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequestDto commentUpdateRequestDto, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(commentService.updateComment(scheduleId, commentId, userId, commentUpdateRequestDto),  HttpStatus.OK);
    }

    /**
     * 특정 ID의 댓글 삭제
     *
     * @param scheduleId 일정 ID
     * @param commentId 댓글 ID
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        commentService.deleteComment(scheduleId, commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
