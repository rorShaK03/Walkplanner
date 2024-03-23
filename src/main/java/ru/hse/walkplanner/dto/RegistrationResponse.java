package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record RegistrationResponse(@JsonProperty(value = "your_id") UUID yourId) {
}
