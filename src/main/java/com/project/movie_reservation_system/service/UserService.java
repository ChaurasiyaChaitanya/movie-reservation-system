package com.project.movie_reservation_system.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.constants.ExceptionConstants;
import com.project.movie_reservation_system.dto.UserRequestDTO;
import com.project.movie_reservation_system.entity.User;
import com.project.movie_reservation_system.enums.UserRole;
import com.project.movie_reservation_system.enums.UserStatus;
import com.project.movie_reservation_system.exception.UserConflictException;
import com.project.movie_reservation_system.exception.UserNotFoundException;
import com.project.movie_reservation_system.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Page<User> getAllUsers(int page, int pageSize)
    {
        return userRepository.findAll(PageRequest.of(page,pageSize));
    }

    public User createNewUser(UserRequestDTO userRequestDTO)
    {
        if(userRepository.findByUsernameOrUserEmail(userRequestDTO.getUserName(),userRequestDTO.getUserEmail()).isPresent())
        {
            throw new UserConflictException("User with the same username or email already exists", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .username(userRequestDTO.getUserName())
                .password(userRequestDTO.getPassword())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .userEmail(userRequestDTO.getUserEmail())
                .userStatus(UserStatus.ACTIVE)
                .userCreatedAt(LocalDateTime.now())
                .userUpdatedAt(LocalDateTime.now())
                .userRole(UserRole.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long userId)
    {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND,HttpStatus.NOT_FOUND));
    }

    public User updateUserById(Long userId, UserRequestDTO userRequestDTO)
    {
        return userRepository
                .findById(userId)
                .map(User -> {
                    User.setFirstName(userRequestDTO.getFirstName());
                    User.setLastName(userRequestDTO.getLastName());
                    User.setPassword(userRequestDTO.getPassword());
                    User.setUserUpdatedAt(LocalDateTime.now());
                    return userRepository.save(User);
                })
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public User promoteUser(User user)
    {
        user.setUserRole(UserRole.ROLE_THEATRE_ADMIN);
        return userRepository.save(user);
    }

    public void deleteUserById(Long userId)
    {
        userRepository.deleteById(userId);
    }

    public User getUserByUserName(String userName)
    {
        return userRepository
                .findByUsername(userName)
                .orElseThrow(() -> new UserNotFoundException(ExceptionConstants.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public boolean isUserPresentByUserNameOrUserEmail(String userName,String userEmail)
    {
        return userRepository.findByUsernameOrUserEmail(userName,userEmail).isPresent();
    }
	
}
