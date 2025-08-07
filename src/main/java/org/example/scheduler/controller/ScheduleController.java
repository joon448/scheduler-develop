package org.example.scheduler.controller;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.dto.schedule.*;
import org.example.scheduler.service.CommentService;
import org.example.scheduler.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final CommentService commentService;

    /**
     * 새로운 일정 등록
     *
     * @param scheduleRequestDto 일정 생성 요청 정보
     * @return 생성된 일정 정보
     */
    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.saveSchedule(scheduleRequestDto);
    }

    /**
     * 전체 일정 목록 또는 특정 작성자의 일정 목록 조회
     *
     * @param name (선택) 작성자명으로 필터링할 경우 사용
     * @return 일정 목록 (최신 수정일 기준 정렬)
     */
    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules(@RequestParam(required = false) String name) {
        if (name == null) {
            return scheduleService.getAllSchedules();
        }
        return scheduleService.getSchedulesByName(name);
    }

    /**
     * 특정 ID의 일정과 그에 달린 댓글 목록 조회
     *
     * @param id 일정 ID
     * @return 일정 + 댓글 정보
     */
    @GetMapping("/schedules/{id}")
    public ScheduleWithCommentsResponseDto getScheduleWithComments(@PathVariable Long id) {
        return scheduleService.getScheduleWithCommentsById(id);
    }

    /**
     * 특정 ID의 일정 수정
     *
     * @param id 일정 ID
     * @param scheduleUpdateRequestDto 일정 수정 요청 정보
     * @return 수정된 일정 정보
     */
    @PatchMapping("/schedules/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        return scheduleService.updateSchedule(id, scheduleUpdateRequestDto);

    }

    /**
     * 특정 ID의 일정 삭제
     *
     * @param id 일정 ID
     * @param scheduleDeleteRequestDto 삭제 요청 정보 (비밀번호)
     */
    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@PathVariable Long id, @RequestBody ScheduleDeleteRequestDto scheduleDeleteRequestDto) {
        scheduleService.deleteSchedule(id, scheduleDeleteRequestDto);
    }

    /**
     * 특정 ID의 일정에 댓글 등록
     *
     * @param scheduleId 일정 ID
     * @param commentRequestDto 댓글 생성 요청 정보
     * @return 생성된 댓글 정보
     */
    @PostMapping("/schedules/{scheduleId}/comments")
    public CommentResponseDto createComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto commentRequestDto){
        return commentService.saveComment(commentRequestDto, scheduleId);
    }

}
