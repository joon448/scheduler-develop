package org.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 유저 Entity
 * - 유저에 대한 정보를 저장
 * - BaseEntity 상속을 통해 작성/수정일 자동 관리
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 일정 ID (자동 생성)

    private String name;    // 작성자명

    private String email;    // 이메일

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
