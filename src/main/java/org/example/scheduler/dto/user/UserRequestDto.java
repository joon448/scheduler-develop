package org.example.scheduler.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 유저 생성 요청 시 전달받는 DTO
 */
@Getter
public class UserRequestDto {
    @NotBlank
    @Size(min = 1, max = 20)
    private String name;    // 유저명
    @NotBlank
    @Email
    private String email;   // 이메일
    @NotBlank
    private String password;
}
