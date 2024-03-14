package ru.hse.walkplanner.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;

public interface DataProviderService {
    @Transactional
    void pushRoute(RouteInfoDTO info);

    @Transactional
    RoutesBrieflyResponse getRoutesBriefly(GetRoutesBrieflyRequest routesRequest, Pageable page);

    @Transactional
    RoutesResponse getRouteDetailed(GetOneRouteRequest routeRequest);
}
