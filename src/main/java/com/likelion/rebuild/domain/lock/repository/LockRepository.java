package com.likelion.rebuild.domain.lock.repository;

import com.likelion.rebuild.domain.lock.entity.Lock;
import com.likelion.rebuild.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LockRepository extends JpaRepository<Lock, Long> {
    Optional<Lock> findByUser(User user);
}
