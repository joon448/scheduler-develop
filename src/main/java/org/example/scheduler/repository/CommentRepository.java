package org.example.scheduler.repository;

import org.example.scheduler.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{

    /**
     * 특정 일정에 해당하는 모든 댓글 조회
     *
     * @param scheduleId 일정 ID
     * @return 댓글 목록
     */
    List<Comment> findByScheduleId(Long scheduleId);

    /**
     * 특정 일정에 등록된 댓글 수 카운트
     *
     * @param scheduleId 일정 ID
     * @return 댓글 개수
     */
    Long countByScheduleId(Long scheduleId);

    /**
     * 특정 일정에 등록된 댓글 삭제
     *
     * @param scheduleId 일정 ID
     */
    void deleteByScheduleId(Long scheduleId);
}
