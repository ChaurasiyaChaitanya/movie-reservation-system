package com.project.movie_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.movie_reservation_system.dto.AuthRequestDTO;
import com.project.movie_reservation_system.dto.AuthResponseDTO;
import com.project.movie_reservation_system.dto.UserRequestDTO;
import com.project.movie_reservation_system.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,AuthenticationService authenticationService)
    {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signUpUser(@RequestBody UserRequestDTO userRequestDTO)
    {
        String authenticationToken = authenticationService.signUpUser(userRequestDTO);
        return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(AuthResponseDTO.builder().authenticationToken(authenticationToken).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getUserName(),authRequestDTO.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String authenticationToken = authenticationService.generateTokenForUser(authRequestDTO.getUserName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthResponseDTO.builder().authenticationToken(authenticationToken).build());
    }
}
