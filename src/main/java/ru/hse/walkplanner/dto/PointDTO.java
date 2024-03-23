package ru.hse.walkplanner.dto;

import lombok.Builder;

@Builder
public record PointDTO(double latitude, double longitude, String[] tags) {
}
