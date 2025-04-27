package com.project.movie_reservation_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.constants.ExceptionConstants;
import com.project.movie_reservation_system.dto.ScreenRequestDTO;
import com.project.movie_reservation_system.dto.SeatRequestDTO;
import com.project.movie_reservation_system.entity.Screen;
import com.project.movie_reservation_system.entity.Seat;
import com.project.movie_reservation_system.entity.Theatre;
import com.project.movie_reservation_system.exception.ScreenNotFoundException;
import com.project.movie_reservation_system.repository.ScreenRepository;

@Service
public class ScreenService {
	
	private final ScreenRepository screenRepository;
    private final SeatService seatService;

    @Autowired
    ScreenService(ScreenRepository screenRepository,SeatService seatService)
    {
        this.screenRepository = screenRepository;
        this.seatService = seatService;
    }

    public Screen getScreenById(Long screenId)
    {
        return screenRepository
                .findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(ExceptionConstants.SCREEN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Screen createNewScreen(Theatre theatre, ScreenRequestDTO screenRequestDTO)
    {
        Screen screen = Screen
                .builder()
                .screenName(screenRequestDTO.getScreenName())
                .theatre(theatre)
                .build();

        List<Seat> seats = new ArrayList<>();

        for(SeatRequestDTO seatRequestDTO : screenRequestDTO.getSeats())
        {
            Seat seat = seatService.createNewSeat(screen,seatRequestDTO);
            seats.add(seat);
        }
        screen.setSeats(seats);
        return screenRepository.save(screen);
    }

}
