package org.example.scheduler.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 댓글 생성 요청 시 전달받는 DTO
 */
@Getter
public class CommentRequestDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String content;     // 내용
}
