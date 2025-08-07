package org.example.scheduler.dto.comment;

import lombok.Getter;

/**
 * 댓글 생성 요청 시 전달받는 DTO
 */
@Getter
public class CommentRequestDto {
    private String name;        // 작성자명
    private String password;    // 비밀번호
    private String content;     // 내용
}
