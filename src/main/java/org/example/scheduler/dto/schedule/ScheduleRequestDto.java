package org.example.scheduler.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 일정 생성 요청 시 전달받는 DTO
 */
@Getter
public class ScheduleRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 30,message = "제목은 최대 30자까지 작성 가능합니다.")
    private String title;       // 제목

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 200, message = "내용은 최대 200자까지 작성 가능합니다.")
    private String content;     // 내용
}
