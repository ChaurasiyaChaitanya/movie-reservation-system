package com.project.movie_reservation_system.service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.constants.ExceptionConstants;
import com.project.movie_reservation_system.entity.Seat;
import com.project.movie_reservation_system.entity.Show;
import com.project.movie_reservation_system.entity.ShowSeat;
import com.project.movie_reservation_system.enums.SeatStatus;
import com.project.movie_reservation_system.exception.SeatAlreadyLockedException;
import com.project.movie_reservation_system.lock.SeatLock;
import com.project.movie_reservation_system.repository.ShowSeatRepository;

@Service
public class ShowSeatService {
	
	private final ShowSeatRepository showSeatRepository;
    private final SeatLock seatLock;

    @Autowired
    ShowSeatService(ShowSeatRepository showSeatRepository,SeatLock seatLock)
    {
        this.showSeatRepository = showSeatRepository;
        this.seatLock = seatLock;
    }

    public ShowSeat createNewShowSeat(Show show, Seat seat)
    {
        ShowSeat showSeat = ShowSeat
                                .builder()
                                .seat(seat)
                                .seatStatus(SeatStatus.AVAILABLE)
                                .show(show)
                                .build();

        return showSeatRepository.save(showSeat);
    }

    public List<ShowSeat> getShowSeats(List<Long> showSeatIds)
    {
        return showSeatRepository.findAllById(showSeatIds);
    }

    public List<ShowSeat> getAvailableSeats(List<Long> showSeatIds)
    {
        return showSeatRepository.findAllById(showSeatIds).stream().filter(showSeat -> showSeat.getSeatStatus().equals(SeatStatus.AVAILABLE)).toList();
    }

    public Double bookSeatsAndGetTotalAmount(List<ShowSeat> showSeats)
    {
        Double amount = 0D;
        for(ShowSeat showSeat : showSeats)
        {
            showSeat.setSeatStatus(SeatStatus.BOOKED);
            amount += showSeat.getSeat().getSeatPrice();
        }
        return amount;
    }

    public void acquireLocksForShowSeats(List<Long> showSeatIds)
    {
        for(Long showSeatId : showSeatIds)
        {
            ReentrantLock reentrantLock = seatLock.getShowSeatLock(showSeatId);
            if(!reentrantLock.tryLock())
            {
                throw new SeatAlreadyLockedException(ExceptionConstants.SEAT_ALREADY_LOCKED, HttpStatus.CONFLICT);
            }
        }
    }

    public void removeLocksForShowSeats(List<Long> showSeatIds)
    {
        for(Long showSeatId : showSeatIds)
        {
            seatLock.removeLockForShowSeat(showSeatId);
        }
    }

}
