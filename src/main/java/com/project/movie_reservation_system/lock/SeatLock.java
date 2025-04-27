package com.project.movie_reservation_system.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class SeatLock {
    private final ConcurrentHashMap<Long, ReentrantLock> currentSeatsLocked = new ConcurrentHashMap<>();

    public ReentrantLock getShowSeatLock(Long showSeatId)
    {
        return currentSeatsLocked.computeIfAbsent(showSeatId, i -> new ReentrantLock());
    }

    public void removeLockForShowSeat(long seatId){
        currentSeatsLocked.remove(seatId);
    }
}
