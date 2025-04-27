package com.project.movie_reservation_system.dto;

import java.time.LocalDateTime;

import com.project.movie_reservation_system.enums.UserStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private UserStatus userStatus;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
}
