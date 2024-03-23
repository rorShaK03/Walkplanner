package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RouteInfoBrieflyDTO;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutePushingInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.entity.User;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.repository.UserRepository;
import ru.hse.walkplanner.service.ApplyAllSpecsService;
import ru.hse.walkplanner.service.DataProviderService;
import ru.hse.walkplanner.service.GetMetricsByMapsApiService;
import ru.hse.walkplanner.service.utils.MapEntityToDTOHelper;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DataProviderServiceImpl implements DataProviderService {

    private TrackRepository trackRepository;
    private UserRepository userRepository;

    private ApplyAllSpecsService applyAllSpecsService;
    private MapEntityToDTOHelper mapEntityToDTOHelper;

    private GetMetricsByMapsApiService metricsService;

    @Transactional
    @Override
    public RegistrationResponse addRandomUser() {
        User user = userRepository.saveAndFlush(new User(null, "stub", "stub", "stub", null, null, null));
        return new RegistrationResponse(user.getId());
    }

    @Transactional
    @Override
    public PushingRouteResponse pushRoute(RoutePushingInfoDTO info) {
        Optional<User> userOpt = userRepository.findById(info.authorId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("no user found with this id");
        }
        User user = userOpt.get();

        Track track = mapEntityToDTOHelper.getTrackEntity(info, user);
        track = trackRepository.saveAndFlush(track);

        metricsService.updateDistanceAndTime(track);

        return new PushingRouteResponse(track.getId());
    }

    @Transactional
    @Override
    public RoutesBrieflyResponse getRoutesBriefly(GetRoutesBrieflyRequest routesRequest, int page, int size, String sort) {
        Specification<Track> spec = applyAllSpecsService.getQuerySpecification(routesRequest, sort);
        Pageable pageable = PageRequest.of(page, size);

        Page<Track> tracksPage = trackRepository.findAll(spec, pageable);
        RouteInfoBrieflyDTO[] content = tracksPage.getContent()
                .stream()
                .map(mapEntityToDTOHelper::getRouteInfoBrieflyDTO)
                .toArray(RouteInfoBrieflyDTO[]::new);

        return RoutesBrieflyResponse.builder()
                .routes(content)
                .currentPage(tracksPage.getNumber())
                .totalPages(tracksPage.getTotalPages())
                .build();
    }

    @Transactional
    @Override
    public RoutesResponse getRouteDetailed(GetOneRouteRequest routeRequest) {
        Optional<Track> trackOpt = trackRepository.findById(routeRequest.id());

        if (trackOpt.isEmpty()) {
            throw new IllegalArgumentException("Can not find track by this authorId: " + routeRequest.id());
        }

        RouteInfoDTO routeInfoDTO = mapEntityToDTOHelper.getRouteInfoDTO(trackOpt.get());
        return new RoutesResponse(routeInfoDTO);
    }
}
