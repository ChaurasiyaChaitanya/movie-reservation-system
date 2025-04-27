package com.project.movie_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.movie_reservation_system.dto.APIResponseDTO;
import com.project.movie_reservation_system.dto.PagedAPIResponseDTO;
import com.project.movie_reservation_system.dto.ReservationRequestDTO;
import com.project.movie_reservation_system.entity.Reservation;
import com.project.movie_reservation_system.service.ReservationService;
import com.project.movie_reservation_system.validation.UserRoleValidationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    ReservationController(ReservationService reservationService, UserRoleValidationService userRoleValidationService)
    {
        this.reservationService = reservationService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllReservations(
            @PathVariable Long userId,
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Reservation> reservations = reservationService.getAllReservationsForUser(userId,page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(reservations.getContent())
                                .totalElements(reservations.getTotalElements())
                                .totalPages(reservations.getTotalPages())
                                .currentLimit(reservations.getNumberOfElements())
                                .build()
                );
    }

    @Secured("ROLE_USER")
    @PostMapping("/reserve")
    public ResponseEntity<APIResponseDTO> createNewReservation(@RequestBody ReservationRequestDTO reservationRequestDTO)
    {
        // Need to verify if the current user is trying to create a reservation for another user
        Reservation newReservation = reservationService.createNewReservation(reservationRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New Movie created with the id: " + newReservation.getReservationId() + " and movie name: "+newReservation.getShow().getMovie().getMovieName()+".")
                                .data(newReservation)
                                .build()
                );
    }

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToCancelReservation(#reservationId)")
    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<APIResponseDTO> cancelReservation(@PathVariable Long reservationId)
    {
        // Need to verify if the current user have permission to cancel the reservation.

        String message = "The reservation Id : "+reservationId+" is already cancelled.";
        if(reservationService.cancelReservation(reservationId))
        {
            message = "Cancelled the reservation Id : "+reservationId+".";
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message(message)
                                .build()
                );
    }
}
