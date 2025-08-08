package org.example.scheduler.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 일정 생성 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleRequestDto {
    @NotBlank
    @Size(min = 1, max = 30)
    private String title;       // 제목
    @NotBlank
    @Size(min = 1, max = 200)
    private String content;     // 내용
}
