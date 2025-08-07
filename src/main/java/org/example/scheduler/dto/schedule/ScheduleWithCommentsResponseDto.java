package org.example.scheduler.dto.schedule;

import lombok.Getter;
import org.example.scheduler.dto.comment.CommentResponseDto;

import java.util.List;

/**
 * 단일 일정 및 댓글 정보를 응답할 때 사용하는 DTO
 */
@Getter
public class ScheduleWithCommentsResponseDto {
    private ScheduleResponseDto schedule;       // 단일 일정 정보
    private List<CommentResponseDto> comments;  // 댓글 정보

    public ScheduleWithCommentsResponseDto(ScheduleResponseDto scheduleResponseDto, List<CommentResponseDto> comments) {
        this.schedule = scheduleResponseDto;
        this.comments = comments;
    }
}