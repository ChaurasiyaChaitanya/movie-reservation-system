package com.project.movie_reservation_system.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.project.movie_reservation_system.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Reservation {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @OneToMany(fetch = FetchType.EAGER)
    List<ShowSeat> seatsReserved;

    private LocalDateTime reservationTime;

    private LocalDateTime updatedTime;

    private Double totalAmount;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus reservationStatus;

}
