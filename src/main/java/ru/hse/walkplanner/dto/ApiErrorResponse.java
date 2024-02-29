package ru.hse.walkplanner.dto;

import lombok.Builder;

@Builder
public record ApiErrorResponse(String exceptionMessage, String description) {
}
