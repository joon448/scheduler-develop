package org.example.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.schedule.ScheduleRequestDto;
import org.example.scheduler.dto.schedule.ScheduleResponseDto;
import org.example.scheduler.dto.schedule.ScheduleUpdateRequestDto;
import org.example.scheduler.dto.schedule.ScheduleWithCommentsResponseDto;
import org.example.scheduler.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

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
     * 전체 일정 목록 또는 특정 작성자의 일정 목록 조회
     *
     * @param userId (선택) 작성자 ID로 필터링할 경우 사용
     * @return 일정 목록 (최신 수정일 기준 정렬)
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(@RequestParam(required = false) Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(scheduleService.getAllSchedules(), HttpStatus.OK);
        }
        return new ResponseEntity<>(scheduleService.getSchedulesByUserId(userId), HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정 및 일정에 달린 댓글 조회
     *
     * @param scheduleId 일정 ID
     * @return 일정 및 댓글
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleWithCommentsResponseDto> getScheduleWithComments(@PathVariable Long scheduleId) {
        return new ResponseEntity<>(scheduleService.getScheduleWithCommentsById(scheduleId), HttpStatus.OK);
    }

    /**
     * 특정 ID의 일정 수정
     *
     * @param scheduleId 일정 ID
     * @param scheduleUpdateRequestDto 일정 수정 요청 정보
     * @return 수정된 일정 정보
     */
    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(scheduleService.updateSchedule(scheduleId, userId, scheduleUpdateRequestDto),  HttpStatus.OK);

    }

    /**
     * 특정 ID의 일정 삭제
     *
     * @param scheduleId 일정 ID
     */
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("userId");
        scheduleService.deleteSchedule(scheduleId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
