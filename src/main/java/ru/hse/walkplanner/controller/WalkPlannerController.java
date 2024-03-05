package ru.hse.walkplanner.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.walkplanner.dto.ApiErrorResponse;
import ru.hse.walkplanner.dto.GetRouteRequest;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.RouteInfoBrieflyDTO;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;

import java.math.BigDecimal;


@RestController
public class WalkPlannerController {

    @GetMapping("/hello")
    public String ahaha() {
        throw new RuntimeException("ahahahhaha");
    }

    @GetMapping("/bye")
    public ResponseEntity<?> ohoho() {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .exceptionMessage("error")
                .description("this is error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @PostMapping("/update/push-route")
    public ResponseEntity<?> pushRoute(@RequestBody RouteInfoDTO request) {
        return ResponseEntity.ok().body(request);
    }

    @GetMapping("/route")
    public ResponseEntity<?> getRoutes(@RequestHeader(value = "apply-filters", defaultValue = "false") String applyFilters,
                                       @RequestBody GetRouteRequest request) {
        RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
                .name("ahaha")
                .userId("12345")
                .description("oh nonono")
                .path(new PointDTO[]{
                        new PointDTO(new BigDecimal("1.1"), new BigDecimal("1.2"), null),
                        new PointDTO(new BigDecimal("1.3"), new BigDecimal("1.112"), null),
                        new PointDTO(new BigDecimal("1.6"), new BigDecimal("1.22"), new String[]{"what", "r", "u", "doing"}),
                        new PointDTO(new BigDecimal("1.89"), new BigDecimal("1.23244"), null)
                })
                .keyPoints(null)
                .build();

        RoutesResponse routesResponse = new RoutesResponse(new RouteInfoDTO[]{routeInfoDTO});
        return ResponseEntity.ok().body(routesResponse);
    }

    @GetMapping("/route-briefly")
    public ResponseEntity<?> getRoutesBriefly(@RequestHeader(value = "apply-filters", defaultValue = "false") String applyFilters,
                                              @RequestBody GetRouteRequest request) {

        RouteInfoBrieflyDTO routeInfoDTO = RouteInfoBrieflyDTO.builder()
                .name("ahaha")
                .userId("12345")
                .description("oh nonono")
                .keyPoints(new String[]{"VDNH", "Red Square"})
                .build();

        RoutesBrieflyResponse routesBrieflyResponse = new RoutesBrieflyResponse(new RouteInfoBrieflyDTO[]{routeInfoDTO});

        return ResponseEntity.ok().body(routesBrieflyResponse);
    }
}
