package com.tobi.MusicLearn_Studio_Backend.modules.music.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Music Composition entity for user-created music
 * Supports draft and published states
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "music_compositions")
public class MusicComposition extends BaseEntity {

    private String userId; // Composer/creator

    private String title;

    private String description;

    private CompositionStatus status; // DRAFT or PUBLISHED

    private Integer bpm; // Beats per minute (tempo)

    private String key; // Musical key (C, D, E, etc.)

    private String scale; // Major, Minor, etc.

    private List<Track> tracks; // Multiple instrument tracks

    private String audioFileUrl; // R2 storage URL for exported MP3 (when published)

    private String coverImageUrl;

    private Double duration; // Duration in minutes

    private Long fileSize; // File size in bytes (for audio file)

    @Builder.Default
    private Boolean isPublic = false; // Whether composition is publicly visible

    public enum CompositionStatus {
        DRAFT,
        PUBLISHED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Track {
        private String id;
        private String name; // e.g., "Piano", "Drums", "Bass"
        private String instrument; // Instrument type
        private List<Note> notes; // Musical notes
        private Integer volume; // 0-100
        private Boolean muted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Note {
        private String pitch; // e.g., "C4", "D#5"
        private Double time; // Time in beats
        private Double duration; // Duration in beats
        private Integer velocity; // 0-127 (MIDI velocity)
    }
}
