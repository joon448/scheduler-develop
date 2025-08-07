package org.example.scheduler.dto.user;

import lombok.Getter;

/**
 * 유저 생성 요청 시 전달받는 DTO
 */
@Getter
public class UserUpdateRequestDto {
    private String name;    // 유저명
    private String email;   // 이메일
}
