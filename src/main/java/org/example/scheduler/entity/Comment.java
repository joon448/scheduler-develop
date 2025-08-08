package org.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @Column(nullable = false, length = 100)
    private String content;     // 내용

    public Comment(String content, Long scheduleId) {
        this.content = content;
    }

    public void updateContent(String content){
        this.content = content;
    }
}
