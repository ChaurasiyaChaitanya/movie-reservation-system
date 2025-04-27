package com.project.movie_reservation_system.entity;

import com.project.movie_reservation_system.enums.SeatType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "row_seat_id", columnNames = {"rowId", "seatNumber"})
})
public class Seat {
	
	@Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long seatId;

    private Integer rowId;

    private Integer seatNumber;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    @ManyToOne()
    @JoinColumn(name = "screen_id")
    private Screen screen;

    private Double seatPrice;
}
