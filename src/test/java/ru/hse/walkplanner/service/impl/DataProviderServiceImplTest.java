package ru.hse.walkplanner.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.KeyPointsDTO;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RoutePushingInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.repository.KeyPointRepository;
import ru.hse.walkplanner.repository.PointRepository;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.repository.UserRepository;
import testContainer.IntegrationEnvironment;

import java.time.Instant;
import java.util.Date;

@SpringBootTest
class DataProviderServiceImplTest extends IntegrationEnvironment {

    @Autowired
    private DataProviderServiceImpl dataProviderServiceImpl;

    @Autowired
    private KeyPointRepository keyPointRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    @Rollback
    void addRandomUser_testCorrectLogic() {
        RegistrationResponse response = dataProviderServiceImpl.addRandomUser();

        Assertions.assertTrue(userRepository.findById(response.yourId()).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void pushRoute_testCorrectLogic() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                .name("name")
                .description("description")
                .authorId(user.yourId())
                .path(new PointDTO[]{
                        new PointDTO(1d, 1d, null),
                        new PointDTO(2d, 2d, null)
                })
                .keyPoints(new KeyPointsDTO[]{
                        new KeyPointsDTO("name", "desc", 1d, 1d),
                        new KeyPointsDTO("name", "desc", 1d, 1d),
                        new KeyPointsDTO("name", "desc", 1d, 1d)
                })
                .build();

        PushingRouteResponse response = dataProviderServiceImpl.pushRoute(routeInfoDTO);


        Assertions.assertTrue(trackRepository.findById(response.entityId()).isPresent());
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, trackRepository.findById(response.entityId()).get().getPoints().size()),
                () -> Assertions.assertEquals(3, trackRepository.findById(response.entityId()).get().getKeyPoints().size()),
                () -> Assertions.assertEquals(trackRepository.findById(response.entityId()).get().getCreator().getId(), user.yourId())
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_NoRequirements() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                .name("name")
                .description("description")
                .authorId(user.yourId())
                .path(new PointDTO[]{
                        new PointDTO(1d, 1d, null),
                        new PointDTO(2d, 2d, null)
                })
                .keyPoints(new KeyPointsDTO[]{
                        new KeyPointsDTO("name", "desc", 1d, 1d),
                        new KeyPointsDTO("name", "desc", 1d, 1d),
                        new KeyPointsDTO("name", "desc", 1d, 1d)
                })
                .build();
        PushingRouteResponse route = dataProviderServiceImpl.pushRoute(routeInfoDTO);
        Track track = trackRepository.findById(route.entityId()).get();


        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(null);
        int page = 0, size = 5;
        String sort = "created_at,desc";

        RoutesBrieflyResponse resp = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(0d, 0d, requirements), page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, (int) resp.totalPages()),
                () -> Assertions.assertEquals(page, (int) resp.currentPage()),
                () -> Assertions.assertEquals(1, resp.routes().length),

                () -> Assertions.assertEquals(resp.routes()[0].id(), track.getId()),
                () -> Assertions.assertEquals(resp.routes()[0].name(), track.getName()),
                () -> Assertions.assertEquals(resp.routes()[0].description(), track.getDescription()),
                () -> Assertions.assertEquals(resp.routes()[0].keyPoints().length, track.getKeyPoints().size()),
                () -> Assertions.assertTrue(track.getCreatedAt().before(Date.from(Instant.now())))
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_anotherPages() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();

        for (int i = 0; i < 17; i++) {
            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("name")
                    .description("description")
                    .authorId(user.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1d, 1d, null),
                            new PointDTO(2d, 2d, null)
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("name", "desc", 1d, 1d),
                            new KeyPointsDTO("name", "desc", 1d, 1d),
                            new KeyPointsDTO("name", "desc", 1d, 1d)
                    })
                    .build();
            dataProviderServiceImpl.pushRoute(routeInfoDTO);
        }


        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(null);
        int size = 5;
        String sort = "created_at,desc";

        RoutesBrieflyResponse respPage0 = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(0d, 0d, requirements), 0, size, sort);
        RoutesBrieflyResponse respPage1 = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(1d, 1d, requirements), 1, size, sort);
        RoutesBrieflyResponse respPage2 = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(2d, 2d, requirements), 2, size, sort);
        RoutesBrieflyResponse respPage3 = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(3d, 3d, requirements), 3, size, sort);
        RoutesBrieflyResponse respPage4 = dataProviderServiceImpl.getRoutesBriefly(new GetRoutesBrieflyRequest(4d, 4d, requirements), 4, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, respPage0.routes().length),
                () -> Assertions.assertEquals(size, respPage1.routes().length),
                () -> Assertions.assertEquals(size, respPage2.routes().length),
                () -> Assertions.assertEquals(2, respPage3.routes().length),
                () -> Assertions.assertEquals(0, respPage4.routes().length)
        );
    }
}