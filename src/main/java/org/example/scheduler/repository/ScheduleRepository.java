package org.example.scheduler.repository;

import org.example.scheduler.entity.Schedule;
import org.example.scheduler.entity.User;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

/**
 * Schedule Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
    List<Schedule> findByUserIdOrderByModifiedAtDesc(Long userId);

    void deleteByUserId(Long userId);

    default Schedule findByIdOrElseThrow(Long scheduleId){
        return findById(scheduleId).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }


//    /**
//     * 특정 작성자명에 해당하는 모든 일정 조회
//     *
//     * @param name 작성자명
//     * @return 일정 목록
//     */
//    List<Schedule> findByName(String name);
}
