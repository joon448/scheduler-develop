package org.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * 댓글 Entity
 * - 일정에 대한 댓글 정보를 저장
 * - BaseEntity 상속을 통해 작성/수정일 자동 관리
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 댓글 ID (자동 생성)

    @Column(nullable = false, length = 20)
    private String name;    // 작성자명

    @Column(nullable = false, length = 50)
    private String password;    // 비밀번호

    @Column(nullable = false, length = 100)
    private String content;     // 내용

    @Column(nullable = false)
    private Long scheduleId;    // 일정 ID

    public Comment(String name, String password, String content, Long scheduleId) {
        this.name = name;
        this.password = password;
        this.content = content;
        this.scheduleId = scheduleId;
    }
}
