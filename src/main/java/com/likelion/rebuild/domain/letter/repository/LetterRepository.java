package com.likelion.rebuild.domain.letter.repository;

import com.likelion.rebuild.domain.letter.entity.Letter;
import com.likelion.rebuild.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findByUserOrderByCreatedAtDesc(User user);
}
