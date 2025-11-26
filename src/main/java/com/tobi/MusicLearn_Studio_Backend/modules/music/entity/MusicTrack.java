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
 * Music Track entity for music sharing (SoundCloud-like)
 * Represents publicly shared music tracks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "music_tracks")
public class MusicTrack extends BaseEntity {

    private String userId; // Artist/uploader

    private String title;

    private String description;

    private String fileUrl; // R2 storage URL for MP3

    private String thumbnailUrl; // R2 storage URL for Cover Image

    private String coverImageUrl;

    private Double duration; // Duration in MINUTES (not seconds!)

    private Long fileSize; // Bytes

    private String waveformData; // JSON array of waveform peaks

    private String genre; // Classical, Jazz, Pop, etc.

    private List<String> tags; // ["piano", "original", "instrumental"]

    @Builder.Default
    private Long playCount = 0L;

    @Builder.Default
    private Long likeCount = 0L;

    @Builder.Default
    private Long commentCount = 0L;

    @Builder.Default
    private Boolean isPublic = true;
}
