package com.likelion.rebuild.domain.breakup.service;

import com.likelion.rebuild.domain.breakup.dto.BreakupHomeResponseDto;
import com.likelion.rebuild.domain.breakup.dto.BreakupSaveRequestDto;
import com.likelion.rebuild.domain.breakup.entity.BreakupEvent;
import com.likelion.rebuild.domain.breakup.repository.BreakupEventRepository;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BreakupService {

    private final BreakupEventRepository breakupEventRepository;
    private final UserRepository userRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 유저당 이별은 1개 - 없으면 새로 생성, 있으면 기존 reason, breakupDate 수정
    @Transactional
    public void saveOrUpdateBreakup(Long userId, BreakupSaveRequestDto request) {

        if (request.getReason() == null || request.getReason().isBlank()
                || request.getBreakupDate() == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        User user = getUserOrThrow(userId);

        BreakupEvent event = breakupEventRepository.findByUser(user)
                .orElse(null);

        if (event == null) {
            // 처음 저장하는 경우
            event = BreakupEvent.builder()
                    .user(user)
                    .reason(request.getReason())
                    .breakupDate(request.getBreakupDate())
                    .build();
        } else {
            // 기존 이별 정보 수정
            event.update(request.getReason(), request.getBreakupDate());
        }

        breakupEventRepository.save(event);
    }

    // 홈 화면용: 오늘 기준 D+N, 이유, 날짜 출력
    @Transactional(readOnly = true)
    public BreakupHomeResponseDto getHomeSummary(Long userId) {
        User user = getUserOrThrow(userId);

        BreakupEvent event = breakupEventRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.BREAKUP_EVENT_NOT_FOUND));

        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(event.getBreakupDate(), today);
        if (days < 0) days = 0;

        String dText = "D+" + days;

        return new BreakupHomeResponseDto(
                event.getReason(),
                event.getBreakupDate(),
                days,
                dText
        );
    }
}
