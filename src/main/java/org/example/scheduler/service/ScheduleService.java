package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.schedule.*;
import org.example.scheduler.entity.Schedule;
import org.example.scheduler.entity.User;
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
 * - 단일 일정 조회 시 댓글 목록도 함께 조회
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    // private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * 일정 저장
     *
     * @param scheduleRequestDto 일정 생성 요청 데이터
     * @return 생성된 일정 응답 DTO
     */
    @Transactional
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto){
        validateScheduleRequest(scheduleRequestDto, "등록");

        User user = userRepository.findByIdOrElseThrow(scheduleRequestDto.getUserId());

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

    /**
     * 작성자명으로 일정 조회
     *
     * @param userId 유저 ID
     * @return 특정 작성자의 일정 목록 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserIdOrderByModifiedAtDesc(userId) // 최신 수정일 기준 내림차순 정렬
                .stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

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
     * @param id 일정 ID
     * @return 특정 일정 + 댓글 목록 응답 DTO (최신 수정일 순 정렬)
     */
    @Transactional(readOnly = true)
    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 특정 일정 수정
     *
     * @param id 일정 ID
     * @param scheduleUpdateRequestDto 일정 수정 요청 데이터
     * @return 수정된 일정 정보 응답 DTO
     */
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {
        //validateScheduleUpdateRequest(scheduleUpdateRequestDto, "수정");
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        //validatePassword(schedule, scheduleUpdateRequestDto.getPassword(), "수정");

//        if(scheduleUpdateRequestDto.getName()!=null){
//            schedule.updateName(scheduleUpdateRequestDto.getName());
//        }
        if(scheduleUpdateRequestDto.getTitle()!=null){
            schedule.updateTitle(scheduleUpdateRequestDto.getTitle());
        }

        scheduleRepository.flush(); // 반환 schedule에 modifiedAt 반영되도록 flush
        return new ScheduleResponseDto(schedule);
    }

    /**
     * 특정 일정 및 관련 댓글 삭제
     *
     * @param id 일정 ID
     */
    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(id);
        //validatePassword(schedule, scheduleDeleteRequestDto.getPassword(), "삭제");

        // commentRepository.deleteByScheduleId(id);
        scheduleRepository.delete(schedule);
    }


    /**
     * 일정 생성 요청 데이터 검증
     * - 작성자명, 비밀번호, 제목, 내용 필수값 확인
     * - 작성자명: 최대 20자, 제목: 최대 30자, 내용: 최대 200자
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateScheduleRequest(ScheduleRequestDto scheduleRequestDto, String action) {
        if (scheduleRequestDto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 유저 ID를 입력해주세요.");
        }
        if (scheduleRequestDto.getTitle() == null || scheduleRequestDto.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 제목을 입력해주세요.");
        }
        if (scheduleRequestDto.getContent() == null || scheduleRequestDto.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 내용을 입력해주세요.");
        }
        if(scheduleRequestDto.getTitle().length() > 30){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 제목은 최대 30자까지 입력 가능합니다.");
        }
        if(scheduleRequestDto.getContent().length() > 200){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 내용은 최대 200자까지 입력 가능합니다.");
        }
    }

    /**
     * 일정 수정 요청 데이터 검증
     * - 작성자명, 비밀번호, 제목 필수값 확인
     * - 작성자명: 최대 20자, 제목: 최대 30자
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateScheduleUpdateRequest(ScheduleUpdateRequestDto scheduleUpdateRequestDto, String action) {
        if(scheduleUpdateRequestDto.getTitle() != null && scheduleUpdateRequestDto.getTitle().length() > 30){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정 "+action+" 실패: 제목은 최대 30자까지 입력 가능합니다.");
        }
    }

//    /**
//     * 비밀번호 일치 검증
//     * @throws ResponseStatusException 유효하지 않은 경우 401 반환
//     */
//    private void validatePassword(Schedule schedule, String password, String action) {
//        if (!schedule.getPassword().equals(password)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "일정 "+action+" 실패: 비밀번호가 일치하지 않습니다.");
//        }
//    }

}
