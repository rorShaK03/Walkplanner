package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RouteInfoBrieflyDTO;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.entity.User;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.repository.TrackRepositoryWithDynamicQuery;
import ru.hse.walkplanner.repository.UserRepository;
import ru.hse.walkplanner.service.DataProviderService;
import ru.hse.walkplanner.service.utils.MapEntityToDTOHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DataProviderServiceImpl implements DataProviderService {

    private TrackRepository trackRepository;
    private TrackRepositoryWithDynamicQuery trackRepositoryWithDynamicQuery;
    private UserRepository userRepository;

    private MapEntityToDTOHelper mapEntityToDTOHelper;

    @Transactional
    @Override
    public RegistrationResponse addRandomUser() {
        User user = userRepository.saveAndFlush(new User(null, "stub", "stub", "stub", null, null, null));
        return new RegistrationResponse(user.getId());
    }

    @Transactional
    @Override
    public PushingRouteResponse pushRoute(RouteInfoDTO info) {
        Optional<User> userOpt = userRepository.findById(info.authorId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("no user found with this id");
        }
        User user = userOpt.get();

        Track track = mapEntityToDTOHelper.getTrackEntity(info, user);
        track = trackRepository.saveAndFlush(track);

        return new PushingRouteResponse(track.getId());
    }

    @Transactional
    @Override
    public RoutesBrieflyResponse getRoutesBriefly(GetRoutesBrieflyRequest routesRequest, int page, int size) {
        List<Sort.Order> orders = parseSort(routesRequest.requirements().sort());
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Track> tracksPage = trackRepositoryWithDynamicQuery.findAllTrackWithRequirements(routesRequest, pagingSort);
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

    private List<Sort.Order> parseSort(String[] sort) {
        try {
            List<Sort.Order> orders = new ArrayList<>();
            for (String s : sort) {
                String[] split = s.split(",");
                Sort.Direction direction = Sort.Direction.ASC;
                if (split[1].equalsIgnoreCase("desc")) {
                    direction = Sort.Direction.DESC;
                }

                orders.add(new Sort.Order(direction, split[0]));
            }
            return orders;
        } catch (Exception ignored) {
            return new ArrayList<>();
        }
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
