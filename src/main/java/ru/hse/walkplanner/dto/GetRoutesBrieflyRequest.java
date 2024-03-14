package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetRoutesBrieflyRequest(@JsonProperty("current_latitude") double latitude,
                                      @JsonProperty("current_longitude") double longitude,
                                      Requirements requirements) {
    public record Requirements(String[] filter,
                               @JsonProperty("contains_key_points") String[] keyPoints,
                               String[] sort) {
    }
}
