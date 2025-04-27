package com.project.movie_reservation_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.constants.ExceptionConstants;
import com.project.movie_reservation_system.dto.ScreenRequestDTO;
import com.project.movie_reservation_system.dto.TheatreAdminRequestDTO;
import com.project.movie_reservation_system.dto.TheatreRequestDTO;
import com.project.movie_reservation_system.entity.Screen;
import com.project.movie_reservation_system.entity.Theatre;
import com.project.movie_reservation_system.entity.TheatreAdmin;
import com.project.movie_reservation_system.entity.User;
import com.project.movie_reservation_system.exception.TheatreNotFoundException;
import com.project.movie_reservation_system.repository.TheatreRepository;

@Service
public class TheatreService {
	
	private final TheatreRepository theatreRepository;
    private final UserService userService;
    private final ScreenService screenService;

    @Autowired
    TheatreService(TheatreRepository theatreRepository, UserService userService, ScreenService screenService)
    {
        this.theatreRepository = theatreRepository;
        this.userService = userService;
        this.screenService = screenService;
    }

    public Page<Theatre> getAllTheatres(int page, int pageSize)
    {
        return theatreRepository.findAll(PageRequest.of(page,pageSize));
    }


    public Theatre getTheatreById(Long theatreId)
    {
        return theatreRepository
                .findById(theatreId)
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND,HttpStatus.NOT_FOUND));
    }

    public Theatre createNewTheatre(TheatreRequestDTO theatreRequestDTO)
    {
        User userTheatreAdmin = userService.getUserById(theatreRequestDTO.getTheatreAdminId());

        Theatre theatre = Theatre
                .builder()
                .theatreName(theatreRequestDTO.getTheatreName())
                .theatreLocation(theatreRequestDTO.getTheatreName())
                .totalBookings(0)
                .totalRevenue(0D)
                .build();

        List<TheatreAdmin> theatreAdmins = new ArrayList<>();
        TheatreAdmin theatreAdmin = createTheatreAdmin(theatre,userTheatreAdmin);
        theatreAdmins.add(theatreAdmin);
        theatre.setTheatreAdmins(theatreAdmins);

        userService.promoteUser(userTheatreAdmin);

        List<Screen> screens = new ArrayList<>();
        for(ScreenRequestDTO screenRequestDTO : theatreRequestDTO.getScreens())
        {
            Screen screen = screenService.createNewScreen(theatre,screenRequestDTO);
            screens.add(screen);
        }
        theatre.setScreens(screens);
        theatre.setTotalScreens(screens.size());
        theatre.setTotalBookings(0);
        return theatreRepository.save(theatre);
    }

    public Theatre updateTheatreById(Long theatreId, TheatreRequestDTO theatreRequestDTO)
    {
        return  theatreRepository
                .findById(theatreId)
                .map(Theatre -> {
                    Theatre.setTheatreName(theatreRequestDTO.getTheatreName());
                    Theatre.setTheatreLocation(theatreRequestDTO.getTheatreLocation());
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre addTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        User userTheatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());
        return  theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(Theatre -> {
                    List<TheatreAdmin> theatreAdmins = Theatre.getTheatreAdmins();
                    TheatreAdmin theatreAdmin = createTheatreAdmin(Theatre,userTheatreAdmin);
                    theatreAdmins.add(theatreAdmin);
                    Theatre.setTheatreAdmins(theatreAdmins);
                    userService.promoteUser(userTheatreAdmin);
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Theatre removeTheatreAdmin(TheatreAdminRequestDTO theatreAdminRequestDTO)
    {
        User userTheatreAdmin = userService.getUserById(theatreAdminRequestDTO.getUserId());
        return  theatreRepository
                .findById(theatreAdminRequestDTO.getTheatreId())
                .map(Theatre -> {
                    List<TheatreAdmin> theatreAdmins = Theatre.getTheatreAdmins();
                    TheatreAdmin theatreAdmin = createTheatreAdmin(Theatre,userTheatreAdmin);
                    theatreAdmins.remove(theatreAdmin);
                    Theatre.setTheatreAdmins(theatreAdmins);
                    // Need to de promote the user if not admin of any other theatre
                    return theatreRepository.save(Theatre);
                })
                .orElseThrow(() -> new TheatreNotFoundException(ExceptionConstants.THEATRE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public TheatreAdmin createTheatreAdmin(Theatre theatre,User theatreAdmin)
    {
        return TheatreAdmin
                .builder()
                .theatre(theatre)
                .user(theatreAdmin)
                .build();
    }

    public void deleteTheatreById(Long theatreId)
    {
        theatreRepository.deleteById(theatreId);
    }

    public void updateTheatre(Theatre theatre)
    {
        theatreRepository.save(theatre);
    }

}
