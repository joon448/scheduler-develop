package org.example.scheduler.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SchedulePageResponseDto {
    private final Long id;                  // 일정 ID
    private final String title;             // 제목
    private final String content;           // 내용
    private final long commentCount;        // 댓글 개수
    private final LocalDateTime createdAt;  // 작성일
    private final LocalDateTime modifiedAt; // 수정일
    private final String userName;          // 유저명
}
