package com.telerikacademy.web.photocontest.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        private UUID contestId;

        @Column(name = "title", nullable = false, unique = true, length = 50)
        private String title;

        @Column(name = "category", nullable = false, length = 50)
        private String category;

        @ManyToOne
        @JoinColumn(name = "phase_id", nullable = false)
        private Phase phase;

        @Column(name = "cover_photo_url", nullable = false, length = 255)
        private String photoUrl;

        @ManyToOne
        @JoinColumn(name = "organizer_id", nullable = false)
        private User organizer;

        @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private LocalDateTime createdAt = LocalDateTime.now();

        @Column(name = "change_phase_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private LocalDateTime changePhaseTime = LocalDateTime.now();

        @Column(name = "is_active")
        private Boolean isActive;
}
