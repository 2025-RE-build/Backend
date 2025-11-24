package com.likelion.rebuild.domain.mission.repository;

import com.likelion.rebuild.domain.mission.entity.UserMission;
import com.likelion.rebuild.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {

    List<UserMission> findByUserAndMissionDate(User user, LocalDate missionDate);

    // 유저가 특정 기간 동안 사용한 미션 id 목록 (중복 방지용, 옵션)
    @Query("""
        select um.mission.id
        from UserMission um
        where um.user = :user
          and um.missionDate between :start and :end
    """)
    List<Long> findMissionIdsByUserAndMissionDateBetween(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
