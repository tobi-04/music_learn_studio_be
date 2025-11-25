package com.tobi.MusicLearn_Studio_Backend.modules.studio.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Music Studio Track entity
 * Represents a single track within a project containing notes and settings
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tracks")
public class Track extends BaseEntity {

    private String projectId; // Reference to Project.id

    private Integer trackNumber; // 0-based index for ordering

    private String name; // Track name (e.g., "Piano 1", "Drums")

    private String instrumentType; // Piano, Guitar, Drum, Bass, Synth

    private String notesData; // JSON string containing note array: [{time, pitch, duration, velocity}]

    @Builder.Default
    private Double volume = 0.8; // 0.0 to 1.0

    @Builder.Default
    private Double pan = 0.0; // -1.0 (left) to 1.0 (right)

    @Builder.Default
    private Boolean muted = false;

    @Builder.Default
    private Boolean solo = false;
}
