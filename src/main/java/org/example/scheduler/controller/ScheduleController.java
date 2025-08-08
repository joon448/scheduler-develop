package org.example.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.comment.CommentRequestDto;
import org.example.scheduler.dto.comment.CommentResponseDto;
import org.example.scheduler.dto.schedule.*;
import org.example.scheduler.service.CommentService;
import org.example.scheduler.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(scheduleService.saveSchedule(scheduleRequestDto, userId), HttpStatus.CREATED);
    }

    /**
     * 전체 일정 목록 조회
     *
     * @return 일정 목록 (최신 수정일 기준 정렬)
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules() {
        return new ResponseEntity<>(scheduleService.getAllSchedules(),  HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정과 그에 달린 댓글 목록 조회
     *
     * @param id 일정 ID
     * @return 일정 + 댓글 정보
     */
//    @GetMapping("/schedules/{id}")
//    public ResponseEntity<ScheduleWithCommentsResponseDto> getScheduleWithComments(@PathVariable Long id) {
//        return new ResponseEntity<>(scheduleService.getScheduleWithCommentsById(id), HttpStatus.OK);
//    }

    /**
     * 특정 ID의 일정 조회
     *
     * @param id 일정 ID
     * @return 일정
     */
    @GetMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.getScheduleById(id), HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정 수정
     *
     * @param id 일정 ID
     * @param scheduleUpdateRequestDto 일정 수정 요청 정보
     * @return 수정된 일정 정보
     */
    @PatchMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(scheduleService.updateSchedule(id, scheduleUpdateRequestDto, userId),  HttpStatus.OK);

    }

    /**
     * 특정 ID의 일정 삭제
     *
     * @param id 일정 ID
     */
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        scheduleService.deleteSchedule(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
