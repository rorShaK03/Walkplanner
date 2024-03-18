package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record GetRoutesBrieflyRequest(@JsonProperty("current_latitude") double latitude,
                                      @JsonProperty("current_longitude") double longitude,
                                      Requirements requirements) {
    public record Requirements(String[] filter,
                               String[] sort) {
    }
}
