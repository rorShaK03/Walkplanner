package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Date;
import java.util.UUID;


@Builder
public record RouteInfoDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("author_username") String authorUsername,
        String name,
        String description,
        PointDTO[] path,
        Double rating,
        @JsonProperty("walked_users") Integer walkedUsers,
        @JsonProperty("distance_meters") Integer distanceMeters,
        @JsonProperty("walk_minutes") Integer walkMinutes,
        @JsonProperty("created_at") Date createdAt,
        @JsonProperty("key_points") KeyPointsDTO[] keyPoints
) {
}
