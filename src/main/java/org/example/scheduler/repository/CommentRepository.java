package org.example.scheduler.repository;

import org.example.scheduler.entity.Comment;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface CommentRepository extends JpaRepository<Comment, Long>{
    /**
     * 특정 일정에 해당하는 모든 댓글 조회 및 수정일 기준 내림차순 정렬
     *
     * @param scheduleId 일정 ID
     * @return 댓글 목록
     */
    List<Comment> findByScheduleIdOrderByModifiedAtDesc(Long scheduleId);

    /**
     * 특정 댓글 조회
     *
     * @param commentId 댓글 ID
     * @return 댓글
     */
    default Comment findByIdOrElseThrow(Long commentId){
        return findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * 특정 일정에 등록된 댓글 삭제
     *
     * @param scheduleId 일정 ID
     */
    void deleteByScheduleId(Long scheduleId);

    /**
     * 특정 유저의 댓글 삭제
     *
     * @param userId 유저 ID
     */
    void deleteByUserId(Long userId);
}
