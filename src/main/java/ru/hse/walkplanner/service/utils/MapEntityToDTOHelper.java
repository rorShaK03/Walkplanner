package ru.hse.walkplanner.service.utils;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.KeyPointsDTO;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.RouteInfoBrieflyDTO;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.entity.KeyPoint;
import ru.hse.walkplanner.entity.Point;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MapEntityToDTOHelper {

    public KeyPointsDTO getKeyPointsDTO(KeyPoint keyPoint) {
        return KeyPointsDTO.builder()
                .name(keyPoint.getName())
                .description(keyPoint.getDescription())
                .latitude(keyPoint.getLatitude())
                .longitude(keyPoint.getLongitude())
                .build();
    }

    public KeyPoint getKeyPointEntity(KeyPointsDTO keyPointsDTO) {
        return new KeyPoint(null, keyPointsDTO.name(), keyPointsDTO.description(),
                keyPointsDTO.latitude(), keyPointsDTO.longitude()
        );
    }

    public List<KeyPoint> getKeyPointEntityList(KeyPointsDTO[] keyPointsDTOS) {
        return Arrays.stream(keyPointsDTOS).map(this::getKeyPointEntity).toList();
    }

    public PointDTO getPointDTO(Point point) {
        return PointDTO.builder()
                .latitude(point.getLatitude())
                .longitude(point.getLongitude())
                .tags(null)
                .build();
    }

    public Point getPointEntity(PointDTO pointDTO, int index) {
        return new Point(null, pointDTO.longitude(), pointDTO.latitude(), index);
    }

    public List<Point> getPointEntityList(PointDTO[] pointDTOs) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointDTOs.length; i++) {
            Point tmp = this.getPointEntity(pointDTOs[i], i);
            points.add(tmp);
        }
        return points;
    }

    public RouteInfoBrieflyDTO getRouteInfoBrieflyDTO(Track track) {
        return RouteInfoBrieflyDTO.builder()
                .keyPoints(track.getKeyPoints().stream().map(this::getKeyPointsDTO).toArray(KeyPointsDTO[]::new))
                .name(track.getName())
                .description(track.getDescription())
                .authorId(track.getCreator().getId())
                .build();
    }

    public RouteInfoDTO getRouteInfoDTO(Track track) {
        return RouteInfoDTO.builder()
                .path(track.getPoints().stream().map(this::getPointDTO).toArray(PointDTO[]::new))
                .keyPoints(track.getKeyPoints().stream().map(this::getKeyPointsDTO).toArray(KeyPointsDTO[]::new))
                .name(track.getName())
                .description(track.getDescription())
                .authorId(track.getCreator().getId())
                .build();
    }

    public Track getTrackEntity(RouteInfoDTO routeInfoDTO, User user) {
        return new Track(null, routeInfoDTO.name(), routeInfoDTO.description(),
                0, 0, 0,
                -1, -1,
                this.getPointEntityList(routeInfoDTO.path()),
                this.getKeyPointEntityList(routeInfoDTO.keyPoints()),
                user
        );
    }
}
