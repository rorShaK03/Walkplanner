package ru.hse.walkplanner.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record KeyPointsDTO(String name, String description, double latitude, double longitude) {
}
