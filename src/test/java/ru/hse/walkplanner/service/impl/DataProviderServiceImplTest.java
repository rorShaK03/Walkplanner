package ru.hse.walkplanner.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

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

    private void pushRoute(int times) {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();

        for (int i = 0; i < times; i++) {
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
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_anotherPages() {
        pushRoute(17);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(null);
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int size = 5;
        String sort = "created_at,desc";

        RoutesBrieflyResponse respPage0 = dataProviderServiceImpl.getRoutesBriefly(request, 0, size, sort);
        RoutesBrieflyResponse respPage1 = dataProviderServiceImpl.getRoutesBriefly(request, 1, size, sort);
        RoutesBrieflyResponse respPage2 = dataProviderServiceImpl.getRoutesBriefly(request, 2, size, sort);
        RoutesBrieflyResponse respPage3 = dataProviderServiceImpl.getRoutesBriefly(request, 3, size, sort);
        RoutesBrieflyResponse respPage4 = dataProviderServiceImpl.getRoutesBriefly(request, 4, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, respPage0.routes().length),
                () -> Assertions.assertEquals(size, respPage1.routes().length),
                () -> Assertions.assertEquals(size, respPage2.routes().length),
                () -> Assertions.assertEquals(2, respPage3.routes().length),
                () -> Assertions.assertEquals(0, respPage4.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterNameIsCorrect() {
        pushRoute(13);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"name=name"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterNameIsCorrect2() {
        pushRoute(13);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"name=am"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterNameFoundZeroRows() {
        pushRoute(13);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"name=AHAHA"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterDescriptionIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"description=d<SEP>e<SEP>s<SEP>c"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterNameAndDescriptionIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"name=name", "description=d<SEP>e<SEP>s<SEP>c"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(size, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterKeyPointsIsCorrect() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();

        for (int i = 0; i < 2; i++) {
            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("name")
                    .description("description")
                    .authorId(user.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1d, 1d, null),
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("keyPoint1", "desc", 1d, 1d),
                    })
                    .build();
            dataProviderServiceImpl.pushRoute(routeInfoDTO);
        }

        for (int i = 0; i < 5; i++) {
            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("name")
                    .description("description")
                    .authorId(user.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1d, 1d, null),
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("keyPoint2", "desc", 1d, 1d),
                    })
                    .build();
            dataProviderServiceImpl.pushRoute(routeInfoDTO);
        }

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"key_points=keyPoint1"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterDistanceToMeIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"distance_to_me_max=0"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterDistanceIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"distance_max=-1"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(0, response.routes().length)
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterRatingIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"rating_min=0"});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, response.routes().length)
        );
    }

    @RepeatedTest(10)
    @Transactional
    @Rollback
    void getRoutesBriefly_checkFilterRatingIsCorrect2() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        for (int i = 0; i < 10; i++) {
            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("name")
                    .description("description")
                    .authorId(user.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1d, 1d, null),
                    })
                    .keyPoints(new KeyPointsDTO[]{})
                    .build();
            PushingRouteResponse route = dataProviderServiceImpl.pushRoute(routeInfoDTO);
            Track track = trackRepository.findById(route.entityId()).get();

            track.setRating(new Random().nextInt(5, 10));
            track.setRatedUsers(i % 5);
        }

        Double val = 2.3;
        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(new String[]{"rating_min=" + val});
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, requirements);
        int page = 0, size = 5;

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, null);

        Assertions.assertAll(
                () -> Assertions.assertTrue(Arrays.stream(response.routes()).allMatch(it -> it.rating() >= val))
        );
    }

    @RepeatedTest(20)
    @Transactional
    @Rollback
    void getRoutesBriefly_checkSortCreatedAtIsCorrect() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        for (int i = 0; i < 10; i++) {
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

            if (i % 2 == 0) {
                track.setCreatedAt(Date.from(Instant.now().plusSeconds(i * 100)));
            } else {
                track.setCreatedAt(Date.from(Instant.now().minusSeconds(i * 100)));
            }
        }

        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, null);
        int page = 0, size = 5;
        String sort = "created_at,desc";

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, response.routes().length),
                () -> Assertions.assertTrue(response.routes()[0].createdAt().after(response.routes()[1].createdAt())),
                () -> Assertions.assertTrue(response.routes()[1].createdAt().after(response.routes()[2].createdAt())),
                () -> Assertions.assertTrue(response.routes()[2].createdAt().after(response.routes()[3].createdAt())),
                () -> Assertions.assertTrue(response.routes()[3].createdAt().after(response.routes()[4].createdAt()))
        );
    }

    @RepeatedTest(10)
    @Transactional
    @Rollback
    void getRoutesBriefly_checkSortDistanceIsCorrect() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        for (int i = 0; i < 10; i++) {
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

            if (i % 2 == 0) {
                track.setDistanceMeters(i * 111);
            } else {
                track.setDistanceMeters(i * 5);
            }
        }


        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, null);
        int page = 0, size = 5;
        String sort = "distance,asc";

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, response.routes().length),
                () -> Assertions.assertTrue(response.routes()[0].distanceMeters() <= (response.routes()[1].distanceMeters())),
                () -> Assertions.assertTrue(response.routes()[1].distanceMeters() <= (response.routes()[2].distanceMeters())),
                () -> Assertions.assertTrue(response.routes()[2].distanceMeters() <= (response.routes()[3].distanceMeters())),
                () -> Assertions.assertTrue(response.routes()[3].distanceMeters() <= (response.routes()[4].distanceMeters()))
        );
    }

    @RepeatedTest(10)
    @Disabled
    @Transactional
    @Rollback
    void getRoutesBriefly_checkSortRatingIsCorrect() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        for (int i = 0; i < 10; i++) {
            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("name")
                    .description("description")
                    .authorId(user.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1d, 1d, null),
                    })
                    .keyPoints(new KeyPointsDTO[]{})
                    .build();
            PushingRouteResponse route = dataProviderServiceImpl.pushRoute(routeInfoDTO);
            Track track = trackRepository.findById(route.entityId()).get();

            track.setRating(new Random().nextInt(0, 5));
            track.setRatedUsers(i % 3);
        }


        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, null);
        int page = 0, size = 5;
        String sort = "rating,desc";

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, response.routes().length),
                () -> Assertions.assertTrue(response.routes()[0].rating() >= response.routes()[1].rating()),
                () -> Assertions.assertTrue(response.routes()[1].rating() >= response.routes()[2].rating()),
                () -> Assertions.assertTrue(response.routes()[2].rating() >= response.routes()[3].rating()),
                () -> Assertions.assertTrue(response.routes()[3].rating() >= response.routes()[4].rating())
        );
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkSortDistanceToMeIsCorrect() {
        pushRoute(6);

        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, null);
        int page = 0, size = 5;
        String sort = "distance_to_me,desc";

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, response.routes().length)
        );
    }


    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_checkMultipleFilterAndSortIsCorrect() {
        pushRoute(333);

        GetRoutesBrieflyRequest.Requirements requirements = new GetRoutesBrieflyRequest.Requirements(
                new String[]{"distance_to_me_min=5", "key_points=name", "name=name"}
        );
        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(110d, 110d, requirements);
        int page = 0, size = 100;
        String sort = "distance_to_me,desc";

        RoutesBrieflyResponse response = dataProviderServiceImpl.getRoutesBriefly(request, page, size, sort);

        Assertions.assertAll(
                () -> Assertions.assertEquals(100, response.routes().length)
        );
    }
}