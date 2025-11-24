package com.likelion.rebuild.domain.breakup.entity;

import com.likelion.rebuild.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "breakup_event",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_breakup_event_user",
                        columnNames = {"user_id"}
                )
        }
)
public class BreakupEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate breakupDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String reason, LocalDate breakupDate) {
        this.reason = reason;
        this.breakupDate = breakupDate;
    }
}