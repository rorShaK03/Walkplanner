package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetOneRouteRequest;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.RouteInfoBrieflyDTO;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.dto.RoutesResponse;
import ru.hse.walkplanner.entity.KeyPoint;
import ru.hse.walkplanner.entity.Point;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.entity.utils.MapEntityToDTOHelper;
import ru.hse.walkplanner.repository.KeyPointRepository;
import ru.hse.walkplanner.repository.PointRepository;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.service.DataProviderService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DataProviderServiceImpl implements DataProviderService {

    private KeyPointRepository keyPointRepository;
    private PointRepository pointRepository;
    private TrackRepository trackRepository;

    private MapEntityToDTOHelper mapEntityToDTOHelper;

    @Transactional
    @Override
    public void pushRoute(RouteInfoDTO info) {
        List<KeyPoint> keyPoints = mapEntityToDTOHelper.getKeyPointEntityList(info.keyPoints());
        keyPointRepository.saveAll(keyPoints);
        List<Point> points = mapEntityToDTOHelper.getPointEntityList(info.path());
        pointRepository.saveAll(points);

        Track track = mapEntityToDTOHelper.getTrackEntity(info);
        trackRepository.save(track);
    }

    @Transactional
    @Override
    public RoutesBrieflyResponse getRoutesBriefly(GetRoutesBrieflyRequest routesRequest, Pageable page) {
//        Map<String, String> parsedFilters = parseRequestHelper.parseFilters(routesRequest.requirements());

        Page<Track> tracksPage = trackRepository.findAll(page);
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
            throw new IllegalArgumentException("Can not find track by this id: " + routeRequest.id());
        }

        RouteInfoDTO routeInfoDTO = mapEntityToDTOHelper.getRouteInfoDTO(trackOpt.get());
        return new RoutesResponse(routeInfoDTO);
    }
}
