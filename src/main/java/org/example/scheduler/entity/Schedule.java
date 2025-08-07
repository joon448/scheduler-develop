package org.example.scheduler.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * 일정 Entity
 * - 일정에 대한 정보를 저장
 * - BaseEntity 상속을 통해 작성/수정일 자동 관리
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 일정 ID (자동 생성)

    // @Column(nullable = false, length = 20)
    // private String name;    // 작성자명

    @Setter
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 50)
    private String password;    // 비밀번호

    @Column(nullable = false, length = 30)
    private String title;   //제목

    @Column(nullable = false, length = 200)
    private String content;     // 내용

    public Schedule(String password, String title, String content) {
        this.password = password;
        this.title = title;
        this.content = content;
    }

    /*
     * 일정 제목을 수정합니다.
     *
     * @param title 수정할 제목
     */
    public void updateTitle(String title) {
        this.title = title;
    }
}
