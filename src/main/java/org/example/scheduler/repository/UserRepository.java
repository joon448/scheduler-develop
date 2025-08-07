package org.example.scheduler.repository;

import org.example.scheduler.entity.Schedule;
import org.example.scheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

/**
 * User Entity에 대한 JPA 리포지토리 인터페이스
 */
public interface UserRepository extends JpaRepository<User, Long>{

    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저 ID입니다."));
    }

}
