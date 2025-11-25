package com.tobi.MusicLearn_Studio_Backend.modules.music.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private String id;
    private String trackId;
    private String userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
