package com.project.movie_reservation_system.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScreenRequestDTO {
    private String screenName;
    List<SeatRequestDTO> seats;
}
