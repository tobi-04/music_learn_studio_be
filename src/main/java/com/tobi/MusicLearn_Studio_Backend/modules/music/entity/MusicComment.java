package com.tobi.MusicLearn_Studio_Backend.modules.music.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Music Comment entity
 * Represents comments on tracks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "music_comments")
public class MusicComment extends BaseEntity {

    private String userId;

    private String trackId;

    private String content;

    private Double timestamp; // Optional: comment at specific time in track (seconds)
}
