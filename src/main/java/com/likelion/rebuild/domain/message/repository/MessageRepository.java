package com.likelion.rebuild.domain.message.repository;

import com.likelion.rebuild.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByCreatedAtAsc();
}
