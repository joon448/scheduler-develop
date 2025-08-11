package org.example.scheduler.repository;

import org.example.scheduler.entity.User;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long>{
    /**
     * 특정 유저ID 조회
     *
     * @param userId 유저 ID
     * @return 유저
     */
    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
    
    /**
     * 특정 이메일이 존재하는지 확인
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 특정 유저이메일 조회
     *
     * @param email 이메일
     * @return 유저
     */
    default User findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
    Optional<User> findByEmail(String email);

    /**
     * 모든 유저 조회 및 수정일 기준 내림차순 정렬
     *
     * @return 유저 목록
     */
    List<User> findAllByOrderByModifiedAtDesc();
}
