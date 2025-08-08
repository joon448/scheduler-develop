package org.example.scheduler.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 유저 생성 요청 시 전달받는 DTO
 */
@Getter
public class UserDeleteRequestDto {
    @NotBlank
    private String password;    // 비밀번호
}
