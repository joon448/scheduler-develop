package org.example.scheduler.repository;

import org.example.scheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Schedule Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{

    /**
     * 특정 작성자명에 해당하는 모든 일정 조회
     *
     * @param name 작성자명
     * @return 일정 목록
     */
    List<Schedule> findByName(String name);
}
