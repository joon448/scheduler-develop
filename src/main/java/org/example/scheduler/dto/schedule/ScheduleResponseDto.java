package org.example.scheduler.dto.schedule;

import lombok.Getter;
import org.example.scheduler.entity.Schedule;

import java.time.LocalDateTime;

/**
 * 일정 정보를 응답할 때 사용하는 DTO
 */
@Getter
public class ScheduleResponseDto {
    private final Long id;                  // 일정 ID
    private final String name;              // 작성자명
    private final String title;             // 제목
    private final String content;           // 내용
    private final LocalDateTime createdAt;  // 작성일
    private final LocalDateTime modifiedAt; // 수정일


    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.name = schedule.getName();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
