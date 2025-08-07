package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.schedule.ScheduleRequestDto;
import org.example.scheduler.dto.user.UserDeleteRequestDto;
import org.example.scheduler.dto.user.UserRequestDto;
import org.example.scheduler.dto.user.UserResponseDto;
import org.example.scheduler.dto.user.UserUpdateRequestDto;
import org.example.scheduler.entity.User;
import org.example.scheduler.repository.ScheduleRepository;
import org.example.scheduler.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 유저 관련 비즈니스 로직을 처리하는 서비스
 * - 유저 생성, 조회, 수정, 삭제 기능 제공
 */
@Service
@RequiredArgsConstructor
public class UserService {
    //private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * 유저 생성
     *
     * @param userRequestDto 유저 생성 요청 데이터
     * @return 생성된 유저 응답 DTO
     */
    @Transactional
    public UserResponseDto saveUser(UserRequestDto userRequestDto){
        validateUserRequest(userRequestDto);
        User user = new User(userRequestDto.getName(), userRequestDto.getEmail(), userRequestDto.getPassword());

        userRepository.save(user);
        return new UserResponseDto(user);
    }

    /**
     * 전체 유저 조회
     *
     * @return 전체 유저 목록 응답 DTO
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll() // 최신 수정일 기준 내림차순 정렬
                .stream()
                .sorted(Comparator.comparing(User::getModifiedAt).reversed())
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 유저 아이디로 유저 조회
     *
     * @param id 유저 ID
     * @return 특정 유저의 정보 응답 DTO
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);
        return new UserResponseDto(user);
    }

    /**
     * 특정 유저 수정
     *
     * @param id 유저 ID
     * @param userUpdateRequestDto 유저 수정 요청 데이터
     * @return 수정된 유저 정보 응답 DTO
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findByIdOrElseThrow(id);
        validateUserUpdateRequest(userUpdateRequestDto);
        validatePasswordMatch(userUpdateRequestDto.getPassword(), user.getPassword());


        if(userUpdateRequestDto.getName()!=null){
            user.updateName(userUpdateRequestDto.getName());
        }

        if(userUpdateRequestDto.getEmail()!=null){
            user.updateEmail(userUpdateRequestDto.getEmail());
        }

        if(userUpdateRequestDto.getNewPassword()!=null){
            user.updatePassword(userUpdateRequestDto.getNewPassword());
        }

        userRepository.flush(); // 반환 user에 modifiedAt 반영되도록 flush
        return new UserResponseDto(user);
    }

    /**
     * 특정 유저 삭제
     *
     * @param id 유저 ID
     * @param userDeleteRequestDto 유저 삭제 요청 데이터
     */
    @Transactional
    public void deleteUser(Long id, UserDeleteRequestDto userDeleteRequestDto) {
        User user = userRepository.findByIdOrElseThrow(id);
        validatePasswordMatch(userDeleteRequestDto.getPassword(), user.getPassword());
        scheduleRepository.deleteByUserId(id); // 해당 유저의 일정 먼저 삭제
        userRepository.delete(user);
    }

    /**
     * 유저 생성 요청 데이터 검증
     * - 작성자명, 비밀번호, 제목, 내용 필수값 확인
     * - 작성자명: 최대 20자, 제목: 최대 30자, 내용: 최대 200자
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateUserRequest(UserRequestDto userRequestDto) {
        if (userRequestDto.getName() == null || userRequestDto.getName().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이름을 입력해주세요.");
        }
        if (userRequestDto.getEmail() == null || userRequestDto.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 입력해주세요.");
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요.");
        }
    }
    /**
     * 유저 생성 요청 데이터 검증
     * - 작성자명, 비밀번호, 제목, 내용 필수값 확인
     * - 작성자명: 최대 20자, 제목: 최대 30자, 내용: 최대 200자
     * @throws ResponseStatusException 유효하지 않은 경우 400 반환
     */
    private void validateUserUpdateRequest(UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getPassword() == null || userUpdateRequestDto.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기존 비밀번호를 입력해주세요.");
        }
        if (userUpdateRequestDto.getName() != null && userUpdateRequestDto.getName().trim().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이름을 입력해주세요.");
        }
        if (userUpdateRequestDto.getEmail() != null && userUpdateRequestDto.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 입력해주세요.");
        }
        if (userUpdateRequestDto.getNewPassword() != null && userUpdateRequestDto.getNewPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호를 입력해주세요.");
        }

    }

    /**
     * 비밀번호 일치 검증
     * @throws ResponseStatusException 유효하지 않은 경우 401 반환
     */
    private void validatePasswordMatch (String inputPassword, String storedPassword) {
        if (!inputPassword.equals(storedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

}
