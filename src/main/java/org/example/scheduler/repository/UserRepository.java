package org.example.scheduler.repository;

import org.example.scheduler.entity.User;
import org.example.scheduler.error.CustomException;
import org.example.scheduler.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long>{

    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    boolean existsByEmail(String email);

    default User findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);
}
