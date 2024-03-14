package ru.hse.walkplanner.dto;

import lombok.Builder;

@Builder
public record RoutesBrieflyResponse(RouteInfoBrieflyDTO[] routes, Integer totalPages, Integer currentPage) {
}
