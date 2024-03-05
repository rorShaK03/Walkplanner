package ru.hse.walkplanner.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record PointDTO(BigDecimal latitude, BigDecimal longitude, String[] tags) {}
