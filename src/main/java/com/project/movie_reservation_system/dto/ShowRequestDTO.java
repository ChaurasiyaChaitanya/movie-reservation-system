package com.project.movie_reservation_system.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowRequestDTO {

    private Long movieId;

    private Long screenId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
