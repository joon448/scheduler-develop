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
    private final String name;              // 작성자명
    private final String content;           // 내용
    private final Long scheduleId;          // 일정 ID
    private final LocalDateTime createdAt;  // 작성일
    private final LocalDateTime modifiedAt; // 수정일


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.name = comment.getName();
        this.content = comment.getContent();
        this.scheduleId = comment.getScheduleId();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
