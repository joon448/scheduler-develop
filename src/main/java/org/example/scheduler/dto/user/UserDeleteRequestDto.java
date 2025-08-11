package org.example.scheduler.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 유저 삭제 요청 시 전달받는 DTO
 */
@Getter
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;    // 비밀번호
}
