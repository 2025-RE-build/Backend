package com.likelion.rebuild.domain.letter.service;

import com.likelion.rebuild.domain.letter.dto.LetterCreateRequestDto;
import com.likelion.rebuild.domain.letter.dto.LetterResponseDto;
import com.likelion.rebuild.domain.letter.entity.Letter;
import com.likelion.rebuild.domain.letter.repository.LetterRepository;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public LetterResponseDto createLetter(Long userId, LetterCreateRequestDto request) {
        User user = getUserOrThrow(userId);

        // 빈 문자열이면 null로 정리
        String toName = (request.getToName() == null || request.getToName().isBlank())
                ? null : request.getToName();
        String fromName = (request.getFromName() == null || request.getFromName().isBlank())
                ? null : request.getFromName();

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Letter letter = Letter.builder()
                .user(user)
                .toName(toName)
                .fromName(fromName)
                .content(request.getContent())
                .build();

        letterRepository.save(letter);

        return new LetterResponseDto(letter);
    }

    @Transactional(readOnly = true)
    public List<LetterResponseDto> getMyLetters(Long userId) {
        User user = getUserOrThrow(userId);

        return letterRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(LetterResponseDto::new)
                .toList();
    }

    @Transactional
    public void deleteLetter(Long letterId, Long userId) {
        User user = getUserOrThrow(userId);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new CustomException(ErrorCode.LETTER_NOT_FOUND));

        if (!letter.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.AUTH_ACCESS_DENIED);
        }

        letterRepository.delete(letter);
    }

    @Transactional
    public LetterResponseDto updateLetter(Long letterId, Long userId, LetterCreateRequestDto request) {
        User user = getUserOrThrow(userId);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new CustomException(ErrorCode.LETTER_NOT_FOUND));

        if (!letter.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.AUTH_ACCESS_DENIED);
        }

        // 빈 문자열이면 null로 정리
        String toName = (request.getToName() == null || request.getToName().isBlank())
                ? null : request.getToName();
        String fromName = (request.getFromName() == null || request.getFromName().isBlank())
                ? null : request.getFromName();

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        letter.updateContent(request.getContent(), toName, fromName);

        return new LetterResponseDto(letter);
    }

    @Transactional(readOnly = true)
    public LetterResponseDto getLetterDetail(Long letterId, Long userId) {
        User user = getUserOrThrow(userId);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new CustomException(ErrorCode.LETTER_NOT_FOUND));

        if (!letter.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.AUTH_ACCESS_DENIED);
        }

        return new LetterResponseDto(letter);
    }
}
