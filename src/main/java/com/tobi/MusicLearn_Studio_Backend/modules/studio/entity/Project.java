package com.tobi.MusicLearn_Studio_Backend.modules.studio.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Music Studio Project entity
 * Represents a music creation project with settings and metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "projects")
public class Project extends BaseEntity {

    private String name;

    private String userId; // Reference to User.id

    private Integer tempo; // BPM (60-240)

    private String keySignature; // C, D, E, F, G, A, B

    private String scaleType; // Major, Minor, Pentatonic, etc.

    private String timeSignature; // 4/4, 3/4, 6/8, etc.

    @Builder.Default
    private Double duration = 0.0; // Total duration in seconds

    @Builder.Default
    private Integer trackCount = 0; // Number of tracks in this project
}
