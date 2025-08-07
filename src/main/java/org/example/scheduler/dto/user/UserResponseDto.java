package org.example.scheduler.dto.user;

import lombok.Getter;
import org.example.scheduler.entity.User;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private Long id;                        // 유저 ID
    private String name;                    // 유저명
    private String email;                   // 이메일
    private final LocalDateTime createdAt;  // 작성일
    private final LocalDateTime modifiedAt; // 수정일

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
