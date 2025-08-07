package org.example.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.login.LoginRequestDto;
import org.example.scheduler.dto.user.UserDeleteRequestDto;
import org.example.scheduler.dto.user.UserRequestDto;
import org.example.scheduler.dto.user.UserResponseDto;
import org.example.scheduler.dto.user.UserUpdateRequestDto;
import org.example.scheduler.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // private final CommentService commentService;

    /**
     * 새로운 유저 등록
     *
     * @param userRequestDto 유저 생성 요청 정보
     * @return 생성된 유저 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.saveUser(userRequestDto), HttpStatus.CREATED);
    }

    /**
     * 전체 유저 목록 조회
     *
     * @return 유저 목록
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),  HttpStatus.OK);
    }

    /**
     * 특정 ID의 유저 조회
     *
     * @param id 유저 ID
     * @return 유저
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * 특정 ID의 유저 수정
     *
     * @param id 유저 ID
     * @param userUpdateRequestDto 유저 수정 요청 정보
     * @return 수정된 유저 정보
     */
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return new ResponseEntity<>(userService.updateUser(id, userUpdateRequestDto),  HttpStatus.OK);

    }

    /**
     * 특정 ID의 유저 삭제
     *
     * @param id 유저 ID
     * @param userDeleteRequestDto 삭제 요청 정보 (비밀번호)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestBody UserDeleteRequestDto userDeleteRequestDto) {
        userService.deleteUser(id, userDeleteRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        userService.login(loginRequestDto, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



//    /**
//     * 특정 ID의 일정에 댓글 등록
//     *
//     * @param scheduleId 일정 ID
//     * @param commentRequestDto 댓글 생성 요청 정보
//     * @return 생성된 댓글 정보
//     */
//    @PostMapping("/schedules/{scheduleId}/comments")
//    public CommentResponseDto createComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto commentRequestDto){
//        return commentService.saveComment(commentRequestDto, scheduleId);
//    }

}
