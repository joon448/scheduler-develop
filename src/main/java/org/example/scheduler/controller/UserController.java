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

    /**
     * 새로운 유저 등록
     *
     * @param userRequestDto 유저 생성 요청 정보
     * @return 생성된 유저 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
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
     * @param userId 유저 ID
     * @return 유저
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * 특정 ID의 유저 수정
     *
     * @param userId 유저 ID
     * @param userUpdateRequestDto 유저 수정 요청 정보
     * @return 수정된 유저 정보
     */
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest httpRequest) {
        Long sessionUserId = (Long) httpRequest.getSession().getAttribute("userId");
        return new ResponseEntity<>(userService.updateUser(userId, sessionUserId, userUpdateRequestDto),  HttpStatus.OK);

    }

    /**
     * 특정 ID의 유저 삭제
     *
     * @param userId 유저 ID
     * @param userDeleteRequestDto 삭제 요청 정보 (비밀번호)
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @Valid @RequestBody UserDeleteRequestDto userDeleteRequestDto, HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        Long sessionUserId = (Long) session.getAttribute("userId");
        userService.deleteUser(userId, sessionUserId, userDeleteRequestDto);
        session.invalidate(); // 유저 삭제 시 세션 종료
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 로그인
     *
     * @param loginRequestDto 로그인 요청 정보
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        userService.login(loginRequestDto, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
