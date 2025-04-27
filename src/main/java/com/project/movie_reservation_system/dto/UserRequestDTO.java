package com.project.movie_reservation_system.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRequestDTO {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String userEmail;
}
