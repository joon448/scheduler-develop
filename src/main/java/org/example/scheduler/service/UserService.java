package org.example.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.example.scheduler.dto.user.UserDeleteRequestDto;
import org.example.scheduler.dto.user.UserRequestDto;
import org.example.scheduler.dto.user.UserResponseDto;
import org.example.scheduler.dto.user.UserUpdateRequestDto;
import org.example.scheduler.entity.User;
import org.example.scheduler.repository.ScheduleRepository;
import org.example.scheduler.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = new User(userRequestDto.getName(), userRequestDto.getEmail());

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

        if(userUpdateRequestDto.getName()!=null){
            user.updateName(userUpdateRequestDto.getName());
        }

        if(userUpdateRequestDto.getEmail()!=null){
            user.updateEmail(userUpdateRequestDto.getEmail());
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

        scheduleRepository.deleteByUserId(id); // 해당 유저의 일정 먼저 삭제
        userRepository.delete(user);
    }



}
