package org.example.scheduler.dto.comment;

import lombok.Getter;
import org.example.scheduler.entity.Comment;

import java.time.LocalDateTime;

/**
 * 댓글 정보를 응답할 때 사용하는 DTO
 */
@Getter
public class CommentResponseDto {
    private final Long id;                  // 댓글 ID
    private final Long userId;              // 유저 ID
    private final Long scheduleId;          // 일정 ID
    private final String content;           // 내용
    private final LocalDateTime createdAt;  // 작성일
    private final LocalDateTime modifiedAt; // 수정일


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.scheduleId = comment.getSchedule().getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
