package org.example.scheduler.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.schedule.*;
import org.example.scheduler.entity.Schedule;
import org.example.scheduler.entity.User;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.example.scheduler.repository.CommentRepository;
import org.example.scheduler.repository.ScheduleRepository;
import org.example.scheduler.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 일정 관련 비즈니스 로직을 처리하는 서비스
 * - 일정 생성, 조회, 수정, 삭제 기능 제공
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * 일정 저장
     *
     * @param scheduleRequestDto 일정 생성 요청 데이터
     * @return 생성된 일정 응답 DTO
     */
    @Transactional
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        Schedule schedule = new Schedule(scheduleRequestDto.getTitle(), scheduleRequestDto.getContent());
        schedule.setUser(user);

        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule);
    }

    /**
     * 전체 일정 조회
     *
     * @return 전체 일정 목록 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getAllSchedules(){
        return scheduleRepository.findAll() // 최신 수정일 기준 내림차순 정렬
                .stream()
                .sorted(Comparator.comparing(Schedule::getModifiedAt).reversed())
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

//    /**
//     * 작성자명으로 일정 조회
//     *
//     * @param userId 유저 ID
//     * @return 특정 작성자의 일정 목록 응답 DTO (최신 수정일 순 정렬)
//     */
//    @Transactional(readOnly = true)
//    public List<ScheduleResponseDto> getSchedulesByUserId(Long userId) {
//        return scheduleRepository.findByUserIdOrderByModifiedAtDesc(userId) // 최신 수정일 기준 내림차순 정렬
//                .stream()
//                .map(ScheduleResponseDto::new)
//                .collect(Collectors.toList());
//    }

//    /**
//     * 특정 일정 및 댓글 조회
//     *
//     * @param id 일정 ID
//     * @return 특정 일정 + 댓글 목록 응답 DTO (최신 수정일 순 정렬)
//     */
//    @Transactional(readOnly = true)
//    public ScheduleWithCommentsResponseDto getScheduleWithCommentsById(Long id) {
//        Schedule schedule = getScheduleOrThrow(id, "조회");
//
//        List<CommentResponseDto> comments = commentRepository.findByScheduleId(id) // 최신 수정일 기준 내림차순 정렬
//                .stream()
//                .sorted(Comparator.comparing(Comment::getModifiedAt).reversed())
//                .map(CommentResponseDto::new)
//                .collect(Collectors.toList());
//
//        return new ScheduleWithCommentsResponseDto(new ScheduleResponseDto(schedule), comments);
//    }

    /**
     * 특정 일정 조회
     *
     * @param scheduleId 일정 ID
     * @return 특정 일정 + 댓글 목록 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 특정 일정 수정
     *
     * @param scheduleId 일정 ID
     * @param scheduleUpdateRequestDto 일정 수정 요청 데이터
     * @return 수정된 일정 정보 응답 DTO
     */
    @Transactional
    public ScheduleResponseDto updateSchedule(Long scheduleId, Long sessionUserId, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        //validateScheduleUpdateRequest(scheduleUpdateRequestDto, "수정");
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        if(!schedule.getUser().getId().equals(sessionUserId)){
            //throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 일정만 수정할 수 있습니다.");
            throw new CustomException(ErrorCode.FORBIDDEN_NOT_OWNER, "본인이 작성한 일정만 수정할 수 있습니다.");
        }

        if(scheduleUpdateRequestDto.getTitle()!=null){
            schedule.updateTitle(scheduleUpdateRequestDto.getTitle());
        }

        if(scheduleUpdateRequestDto.getContent()!=null){
            schedule.updateContent(scheduleUpdateRequestDto.getContent());
        }

        scheduleRepository.flush(); // 반환 schedule에 modifiedAt 반영되도록 flush
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 특정 일정 및 관련 댓글 삭제
     *
     * @param scheduleId 일정 ID
     */
    @Transactional
    public void deleteSchedule(Long scheduleId, Long sessionUserId) {

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        if(!schedule.getUser().getId().equals(sessionUserId)){
            //throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 일정만 삭제할 수 있습니다.");
            throw new CustomException(ErrorCode.FORBIDDEN_NOT_OWNER, "본인이 작성한 일정만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByScheduleId(scheduleId);
        scheduleRepository.delete(schedule);
    }
}
