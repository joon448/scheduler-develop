package org.example.scheduler.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 유저 생성 요청 시 전달받는 DTO
 */
@Getter
public class UserUpdateRequestDto {
    @Size(min = 1, max = 20)
    private String name;    // 유저명
    @Email
    private String email;   // 이메일
    @NotBlank
    private String password; // 기존 비밀번호
    private String newPassword; // 새 비밀번호
}
