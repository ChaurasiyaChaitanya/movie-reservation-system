package com.project.movie_reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.dto.SeatRequestDTO;
import com.project.movie_reservation_system.entity.Screen;
import com.project.movie_reservation_system.entity.Seat;
import com.project.movie_reservation_system.enums.SeatType;
import com.project.movie_reservation_system.repository.SeatRepository;

@Service
public class SeatService {
	
	private final SeatRepository seatRepository;

    @Autowired
    SeatService(SeatRepository seatRepository)
    {
        this.seatRepository = seatRepository;
    }

    public Seat createNewSeat(Screen screen,SeatRequestDTO seatRequestDTO)
    {
        Seat seat = Seat
                .builder()
                .rowId(seatRequestDTO.getRowId())
                .seatNumber(seatRequestDTO.getSeatNumber())
                .seatPrice(seatRequestDTO.getSeatPrice())
                .seatType(SeatType.valueOf(seatRequestDTO.getSeatType()))
                .screen(screen)
                .build();

        return seatRepository.save(seat);
    }

}
