package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record GetRoutesBrieflyRequest(@JsonProperty("current_latitude") BigDecimal latitude,
                                      @JsonProperty("current_longitude") BigDecimal longitude,
                                      Requirements requirements) {
    public record Requirements(String[] filter, @JsonProperty("contains_key_points") String[] keyPoints) {
    }
}
