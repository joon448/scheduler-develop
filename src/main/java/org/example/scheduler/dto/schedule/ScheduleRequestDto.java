package org.example.scheduler.dto.schedule;

import lombok.Getter;

/**
 * 일정 생성 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleRequestDto {
    private String name;        // 작성자명
    private String password;    // 비밀번호
    private String title;       // 제목
    private String content;     // 내용
}
