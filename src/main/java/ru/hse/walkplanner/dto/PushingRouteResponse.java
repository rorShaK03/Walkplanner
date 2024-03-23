package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PushingRouteResponse(@JsonProperty(value = "entity_id") UUID entityId) {
}
