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
    @Size(min = 1, max = 20, message = "이름은 최대 20자까지 작성 가능합니다.")
    private String name;    // 유저명

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;   // 이메일

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password; // 기존 비밀번호

    private String newPassword; // 새 비밀번호
}
