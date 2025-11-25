package com.tobi.MusicLearn_Studio_Backend.modules.studio.entity;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Music Studio Recording entity
 * Represents audio recordings uploaded or recorded within a project
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "recordings")
public class Recording extends BaseEntity {

    private String projectId; // Reference to Project.id

    private String userId; // Who recorded/uploaded this

    private String fileUrl; // R2 storage URL

    private String fileName;

    private Double duration; // Duration in seconds

    private Integer sampleRate; // 44100, 48000, etc.

    private String format; // webm, wav, mp3

    private Long fileSize; // Size in bytes
}
