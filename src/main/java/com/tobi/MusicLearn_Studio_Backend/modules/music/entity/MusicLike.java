package com.tobi.MusicLearn_Studio_Backend.modules.music.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Music Like entity
 * Represents user likes on tracks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "music_likes")
@CompoundIndexes({
        @CompoundIndex(name = "user_track_idx", def = "{'userId': 1, 'trackId': 1}", unique = true),
        @CompoundIndex(name = "track_idx", def = "{'trackId': 1}")
})
public class MusicLike extends BaseEntity {

    private String userId; // User who liked the track

    private String trackId; // Track that was liked
}
