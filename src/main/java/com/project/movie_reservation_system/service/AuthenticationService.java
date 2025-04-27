package com.project.movie_reservation_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.dto.UserRequestDTO;
import com.project.movie_reservation_system.entity.User;
import com.project.movie_reservation_system.exception.UserConflictException;

@Service
public class AuthenticationService {
	
	private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    AuthenticationService(UserService userService,JwtService jwtService,BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String signUpUser(UserRequestDTO userRequestDTO) {
        if(userService.isUserPresentByUserNameOrUserEmail(userRequestDTO.getUserName(),userRequestDTO.getUserEmail()))
        {
            throw new UserConflictException("User with the same username or email already exists", HttpStatus.CONFLICT);
        }
        encodePassword(userRequestDTO);
        User newUser = userService.createNewUser(userRequestDTO);
        return jwtService.generateJwtToken(newUser);
    }

    public void encodePassword(UserRequestDTO userRequestDTO)
    {
    	userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
    }

    public String generateTokenForUser(String userName) {
        User user = userService.getUserByUserName(userName);
        return jwtService.generateJwtToken(user);
    }

}
