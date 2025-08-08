package org.example.scheduler.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 일정 수정 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleUpdateRequestDto {
    @NotBlank
    @Size(min = 1, max = 30)
    private String title;       // 제목
}
