package org.example.scheduler.repository;

import org.example.scheduler.entity.Schedule;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Schedule Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * 특정 유저의 모든 일정 조회 및 수정일 기준 내림차순 정렬
     *
     * @param userId 유저 ID
     * @return 일정 목록
     */
    List<Schedule> findByUserIdOrderByModifiedAtDesc(Long userId);

    /**
     * 특정 일정 조회
     *
     * @param scheduleId 일정 ID
     * @return 일정
     */
    default Schedule findByIdOrElseThrow(Long scheduleId) {
        return findById(scheduleId).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    /**
     * 모든 일정 조회 및 수정일 기준 내맄차순 정렬
     *
     * @return 일정 목록
     */
    List<Schedule> findAllByOrderByModifiedAtDesc();

    /**
     * 특정 유저의 일정 삭제
     *
     * @param userId 유저 ID
     */
    void deleteByUserId(Long userId);
}
