package ru.hse.walkplanner.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.walkplanner.dto.ApiErrorResponse;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RoutePushingInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;
import ru.hse.walkplanner.service.DataProviderService;


@RestController
@AllArgsConstructor
public class WalkPlannerController {

    private DataProviderService dataProviderService;

    @GetMapping("/throw-exception")
    public String ahaha() {
        throw new RuntimeException("ahahahhaha");
    }

    @GetMapping("/return-error-response")
    public ResponseEntity<ApiErrorResponse> ohoho() {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .exceptionMessage("error")
                .description("this is error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @PostMapping("/user/sign-up")
    public ResponseEntity<RegistrationResponse> signUpUser() {
        RegistrationResponse response = dataProviderService.addRandomUser();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update/push-route")
    public ResponseEntity<PushingRouteResponse> pushRoute(@RequestBody RoutePushingInfoDTO request) {
        PushingRouteResponse response = dataProviderService.pushRoute(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/route")
    public ResponseEntity<RoutesResponse> getRoute(@RequestBody GetOneRouteRequest request) {
        RoutesResponse response = dataProviderService.getRouteDetailed(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/routes-briefly")
    public ResponseEntity<RoutesBrieflyResponse> getRoutesBriefly(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "[created_at,desc]") String sort,
            @RequestBody GetRoutesBrieflyRequest request
    ) {
        RoutesBrieflyResponse response = dataProviderService.getRoutesBriefly(request, page, size, sort);
        return ResponseEntity.ok().body(response);
    }
}
