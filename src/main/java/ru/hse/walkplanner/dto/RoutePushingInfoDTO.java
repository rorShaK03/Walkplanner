package ru.hse.walkplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;


@Builder
public record RoutePushingInfoDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("author_id") UUID authorId,
        String name,
        String description,
        PointDTO[] path,
        @JsonProperty("key_points") KeyPointsDTO[] keyPoints
) {
}
