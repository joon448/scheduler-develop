package org.example.scheduler.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.login.LoginRequestDto;
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

        if (userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 존재하는 이메일입니다.");
        }
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
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto, Long sessionUserId) {
        User user = userRepository.findByIdOrElseThrow(id);
        if(!user.getId().equals(sessionUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 정보만 수정할 수 있습니다.");
        }

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
    public void deleteUser(Long id, UserDeleteRequestDto userDeleteRequestDto,  Long sessionUserId) {
        User user = userRepository.findByIdOrElseThrow(id);
        if(!user.getId().equals(sessionUserId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인만 삭제할 수 있습니다.");
        }
        validatePasswordMatch(userDeleteRequestDto.getPassword(), user.getPassword());
        scheduleRepository.deleteByUserId(id); // 해당 유저의 일정 먼저 삭제
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = userRepository.findByEmailOrElseThrow(loginRequestDto.getEmail());

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        request.getSession().setAttribute("userId", user.getId());
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
