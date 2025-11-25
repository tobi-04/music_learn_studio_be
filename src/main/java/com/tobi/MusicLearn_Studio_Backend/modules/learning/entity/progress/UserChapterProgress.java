package com.tobi.MusicLearn_Studio_Backend.modules.learning.entity.progress;

import com.tobi.MusicLearn_Studio_Backend.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user_chapter_progress")
@CompoundIndex(name = "user_chapter_idx", def = "{'userId': 1, 'chapterId': 1}", unique = true)
public class UserChapterProgress extends BaseEntity {

    private String userId;

    private String chapterId;

    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    private Integer timeSpentMinutes = 0;
}
