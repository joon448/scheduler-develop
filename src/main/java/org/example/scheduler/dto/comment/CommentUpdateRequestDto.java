package org.example.scheduler.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 댓글 수정 요청 시 전달받는 DTO
 */
@Getter
public class CommentUpdateRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 100, message = "내용은 최대 100자까지 작성 가능합니다.")
    private String content;     // 내용
}
