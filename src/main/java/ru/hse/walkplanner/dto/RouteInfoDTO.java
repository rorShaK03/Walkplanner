package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record RouteInfoDTO(@NotNull @JsonProperty("user_id") String userId, String name,
                           String description, PointDTO[] path,
                           @JsonProperty("key_points") KeyPointsDTO[] keyPoints) {
}
