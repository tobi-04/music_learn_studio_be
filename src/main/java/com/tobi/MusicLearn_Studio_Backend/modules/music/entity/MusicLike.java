package com.tobi.MusicLearn_Studio_Backend.modules.music.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Music Like entity
 * Represents user likes on tracks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "music_likes")
@CompoundIndex(name = "user_track_idx", def = "{'userId': 1, 'trackId': 1}", unique = true)
public class MusicLike {

    private String id;

    private String userId;

    private String trackId;

    private LocalDateTime createdAt;
}
