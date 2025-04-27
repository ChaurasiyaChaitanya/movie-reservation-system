package com.project.movie_reservation_system.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TheatreRequestDTO {
    private String theatreName;
    private String theatreLocation;
    private Long theatreAdminId;

    List<ScreenRequestDTO> screens;
}
