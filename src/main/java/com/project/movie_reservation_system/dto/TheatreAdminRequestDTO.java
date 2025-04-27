package com.project.movie_reservation_system.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TheatreAdminRequestDTO {
    Long userId;
    Long theatreId;
}