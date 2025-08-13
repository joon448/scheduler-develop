package org.example.scheduler.repository;

import org.example.scheduler.dto.schedule.SchedulePageResponseDto;
import org.example.scheduler.entity.Schedule;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * Schedule Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

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
     * 모든 일정 (페이지) 조회 및 수정일 기준 내림차순 정렬
     *
     * @param pageable 페이지 정보
     * @return 일정 목록
     */
    @Query(
        value = """
            select new org.example.scheduler.dto.schedule.SchedulePageResponseDto (
                 s.id, s.title, s.content, (select count(c.id) from Comment c where c.schedule = s), s.createdAt, s.modifiedAt, u.name
            )
            from Schedule s join s.user u
            order by s.modifiedAt desc
        """,
        countQuery = """
            select count(s.id)
            from Schedule s
        """
    )
    Page<SchedulePageResponseDto> findPageByOrderByModifiedAtDesc(Pageable pageable);

    /**
     * 특정 유저의 모든 일정 (페이지) 조회 및 수정일 기준 내림차순 정렬
     *
     * @param userId 유저 ID
     * @param pageable 페이지 정보
     * @return 일정 목록
     */
    @Query(
        value = """
            select new org.example.scheduler.dto.schedule.SchedulePageResponseDto (
                 s.id, s.title, s.content, (select count(c.id) from Comment c where c.schedule = s), s.createdAt, s.modifiedAt, u.name
             )
            from Schedule s join s.user u
            where u.id = :userId
            order by s.modifiedAt desc
        """,
        countQuery = """
            select count(s.id)
            from Schedule s join s.user u
            where u.id = :userId
        """
    )
    Page<SchedulePageResponseDto> findPageByUserIdOrderByModifiedAtDesc(Long userId, Pageable pageable);

    /**
     * 특정 유저의 일정 삭제
     *
     * @param userId 유저 ID
     */
    void deleteByUserId(Long userId);

}
