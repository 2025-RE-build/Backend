package com.likelion.rebuild.domain.support.service;

import com.likelion.rebuild.domain.breakup.entity.BreakupEvent;
import com.likelion.rebuild.domain.breakup.repository.BreakupEventRepository;
import com.likelion.rebuild.domain.support.dto.SupportMessageResponseDto;
import com.likelion.rebuild.domain.user.entity.User;
import com.likelion.rebuild.domain.user.repository.UserRepository;
import com.likelion.rebuild.global.exception.CustomException;
import com.likelion.rebuild.global.exception.ErrorCode;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SupportMessageService {

    private final OpenAIClient openAIClient;
    private final UserRepository userRepository;
    private final BreakupEventRepository breakupEventRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private BreakupEvent getBreakupOrThrow(User user) {
        return breakupEventRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.BREAKUP_EVENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public SupportMessageResponseDto getTodaySupportMessage(Long userId) {
        User user = getUserOrThrow(userId);
        BreakupEvent breakup = getBreakupOrThrow(user);

        long days = ChronoUnit.DAYS.between(
                breakup.getBreakupDate(),
                LocalDate.now()
        );
        if (days < 0) days = 0;

        String prompt = """
                이별한지 %d일 된 사람을 위로하는 한국어 2줄 응원 문구를 만들어줘.
                조건:
                - 따옴표 없이 문장만 출력
                - 존댓말
                - 각 줄은 최대 40자
                - 상대방/전애인 언급보다 '나' 자신을 돌보는 내용
                """.formatted(days);

        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .model("gpt-4.1-mini")   // 비용 싸게 가고 싶으면 mini 계열 추천
                    .addSystemMessage("너는 다정한 이별 회복 코치야. 사용자를 위로하는 짧은 문장을 만든다.")
                    .addUserMessage(prompt)
                    .build();

            ChatCompletion completion =
                    openAIClient.chat().completions().create(params);

            String text = completion.choices().stream()
                    .findFirst()
                    .flatMap(choice -> choice.message().content()) // Optional<String>
                    .orElseThrow(() -> new IllegalStateException("GPT 응답이 비어있어요"))
                    .trim();

            return new SupportMessageResponseDto(text);

        } catch (Exception e) {
            // 혹시 API 실패하면 기본 문구 하나 리턴 (홈이 안 깨지게)
            String fallback = "헤어진 지금도, 당신은 충분히 소중한 사람이에요.";
            return new SupportMessageResponseDto(fallback);
        }
    }
}
