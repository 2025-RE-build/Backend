package com.likelion.rebuild.domain.mission.config;

import com.likelion.rebuild.domain.mission.entity.Mission;
import com.likelion.rebuild.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MissionDataInitializer {

    private final MissionRepository missionRepository;

    @Bean
    public CommandLineRunner initMissions() {
        return args -> {
            if (missionRepository.count() > 0) {
                return;
            }

            List<Mission> missions = List.of(
                    Mission.builder().content("10분만 산책하면서 바람 느낌 느껴보기").build(),
                    Mission.builder().content("나에게 위로 편지 5줄 써보기").build(),
                    Mission.builder().content("오늘 고마웠던 일 3가지 적어보기").build(),
                    Mission.builder().content("핸드폰 내려놓고 10분간 스트레칭하기").build(),
                    Mission.builder().content("따뜻한 차 한 잔 마시면서 쉬기").build(),
                    Mission.builder().content("지금 내 감정을 단어 하나로 표현해보기").build(),
                    Mission.builder().content("내가 좋아하는 노래 한 곡 꼭 듣기").build(),
                    Mission.builder().content("내 방/책상 5분만 정리해보기").build(),
                    Mission.builder().content("미래의 나에게 메모 한 줄 보내기").build(),
                    Mission.builder().content("‘괜찮아’라는 말을 오늘 하루 세 번 나에게 해주기").build(),
                    Mission.builder().content("5분 동안 천천히 깊게 숨 쉬기").build(),
                    Mission.builder().content("오늘 하루 나를 웃게 한 순간 떠올리기").build(),
                    Mission.builder().content("내가 잘하고 있는 점 3개 적어보기").build(),
                    Mission.builder().content("지금 가장 따뜻한 장소에 5분 머물러 보기").build(),
                    Mission.builder().content("부정적인 생각 들면 메모장에 털어내기").build(),
                    Mission.builder().content("연락하고 싶었던 친구에게 안부 인사 보내기").build(),
                    Mission.builder().content("오늘 하루 나에게 친절하게 말하기").build(),
                    Mission.builder().content("오늘 하고 싶은 일 하나 정해서 실천해보기").build(),
                    Mission.builder().content("좋아하는 음식 하나 먹기").build(),
                    Mission.builder().content("영상 하나 골라서 ‘나를 위해’ 시청하기").build(),
                    Mission.builder().content("10분간 가벼운 홈트나 스트레칭 하기").build(),
                    Mission.builder().content("기분 좋은 향을 맡아보기 (향수, 섬유유연제 등)").build(),
                    Mission.builder().content("이어폰 없이 걸으며 주변 소리 들어보기").build(),
                    Mission.builder().content("사진첩에서 나 혼자 웃고 있는 사진 찾아보기").build(),
                    Mission.builder().content("SNS에서 보기 싫은 계정 1개 숨기기").build(),
                    Mission.builder().content("물 한 컵 마시고 잠깐 눈 감고 호흡하기").build(),
                    Mission.builder().content("오늘 나에게 선물하고 싶은 말 한 줄 적기").build(),
                    Mission.builder().content("지금 떠오르는 걱정거리 3개 적고 옆에 반박 적기").build(),
                    Mission.builder().content("내 컨디션을 1~10점으로 매겨보기").build(),
                    Mission.builder().content("‘지금 나에게 필요한 것 1가지’ 적어보기").build()
            );

            missionRepository.saveAll(missions);
        };
    }
}
