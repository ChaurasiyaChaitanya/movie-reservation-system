package com.project.movie_reservation_system.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReservationRequestDTO {

    private Long userId;
    private Long showId;
    private List<ShowSeatRequestDTO> showSeats;
}