package com.tobi.MusicLearn_Studio_Backend.modules.music.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Tracks when users listen to music tracks for "Recently Played" feature
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "music_listening_history")
@CompoundIndex(name = "user_listened_at", def = "{'userId': 1, 'listenedAt': -1}")
public class MusicListeningHistory extends BaseEntity {

    @Indexed
    private String userId;

    @Indexed
    private String trackId;

    @Builder.Default
    private LocalDateTime listenedAt = LocalDateTime.now();
}
