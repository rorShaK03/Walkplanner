package ru.hse.walkplanner.service;

import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutePushingInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;

public interface DataProviderService {

    RegistrationResponse addRandomUser();

    PushingRouteResponse pushRoute(RoutePushingInfoDTO info);

    RoutesBrieflyResponse getRoutesBriefly(GetRoutesBrieflyRequest routesRequest, int page, int size, String sort);

    RoutesResponse getRouteDetailed(GetOneRouteRequest routeRequest);
}
