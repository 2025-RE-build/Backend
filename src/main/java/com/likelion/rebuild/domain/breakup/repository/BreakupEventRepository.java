package com.likelion.rebuild.domain.breakup.repository;

import com.likelion.rebuild.domain.breakup.entity.BreakupEvent;
import com.likelion.rebuild.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreakupEventRepository extends JpaRepository<BreakupEvent, Long> {

    // 유저당 이별은 1개라고 가정
    Optional<BreakupEvent> findByUser(User user);
}
