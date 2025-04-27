package com.project.movie_reservation_system.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagedAPIResponseDTO {
    List<?> pageData;
    long totalElements;
    int totalPages;
    int currentLimit;
}
