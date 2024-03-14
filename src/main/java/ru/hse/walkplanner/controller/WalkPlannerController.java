package ru.hse.walkplanner.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.walkplanner.dto.ApiErrorResponse;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;
import ru.hse.walkplanner.service.DataProviderService;

import java.util.ArrayList;
import java.util.List;


@RestController
@AllArgsConstructor
public class WalkPlannerController {

    private DataProviderService dataProviderService;


//    public WalkPlannerController(UserRepository userRepository, TrackRepository trackRepository, PointRepository pointRepository, KeyPointRepository keyPointRepository) {
//        // Просто пример использования репозитория
//        Point point = new Point(null, 0.3131231, 0.47287482347, 0, null);
//        pointRepository.save(point);
//        KeyPoint keyPoint = new KeyPoint(null, "keyPointName", "keyPointDescription", 0.3131231, 0.47287482347, null);
//        keyPointRepository.save(keyPoint);
//        User user = new User(null, "kirill", "myemail@ya.ru", "some_hash", List.of(), null, null);
//        userRepository.save(user);
//        Track track = new Track(null, "name", "desc", 0, 0,0, 0, 0, List.of(point), List.of(keyPoint), user, null, null);
//        trackRepository.save(track);
//    }

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
        dataProviderService.pushRoute(request);
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/route")
    public ResponseEntity<RoutesResponse> getRoute(@RequestBody GetOneRouteRequest request) {
        RoutesResponse response = dataProviderService.getRouteDetailed(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/routes-briefly")
    public ResponseEntity<RoutesBrieflyResponse> getRoutesBriefly(
            @RequestHeader(value = "apply-filters", defaultValue = "false") String applyFilters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "[id,desc]") String[] sort,
            @RequestBody GetRoutesBrieflyRequest request
    ) {

        List<Order> orders = parseSort(sort);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        RoutesBrieflyResponse response = dataProviderService.getRoutesBriefly(request, pagingSort);
        return ResponseEntity.ok().body(response);
    }

    private List<Order> parseSort(String[] sort) {
        List<Order> orders = new ArrayList<>();
        for (String s : sort) {
            String[] split = s.split(",");
            Sort.Direction direction = Sort.Direction.ASC;
            if (split[1].equals("desc")) {
                direction = Sort.Direction.DESC;
            }

            orders.add(new Order(direction, split[0]));
        }
        return orders;
    }
}
