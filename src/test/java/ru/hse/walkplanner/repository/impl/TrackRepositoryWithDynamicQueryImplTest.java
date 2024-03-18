package ru.hse.walkplanner.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.KeyPointsDTO;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.dto.RoutesBrieflyResponse;
import ru.hse.walkplanner.entity.KeyPoint;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.ApplyAnyFilterService;
import ru.hse.walkplanner.service.ApplyAnySortingService;
import ru.hse.walkplanner.service.DataProviderService;
import testContainer.IntegrationEnvironment;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@SpringBootTest
class TrackRepositoryWithDynamicQueryImplTest extends IntegrationEnvironment {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ApplyAnyFilterService applyAnyFilterService;
    @Autowired
    private ApplyAnySortingService applyAnySortingService;
    @Autowired
    private DataProviderService dataProviderService;

    private TrackRepositoryWithDynamicQueryImpl trackRepositoryWithDynamicQueryImpl;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void init() {
        trackRepositoryWithDynamicQueryImpl = new TrackRepositoryWithDynamicQueryImpl(entityManager, applyAnyFilterService, applyAnySortingService);
    }

    @SneakyThrows
    private String invokeGetSqlQueryMethod(GetRoutesBrieflyRequest.Requirements requirements, Sort sort, String[] info) {
        Method method = TrackRepositoryWithDynamicQueryImpl.class.getDeclaredMethod("getSqlQuery", GetRoutesBrieflyRequest.Requirements.class, Sort.class, String[].class);
        method.setAccessible(true);
        return (String) method.invoke(trackRepositoryWithDynamicQueryImpl, requirements, sort, info);
    }

    @Test
    void getSqlQuery_testWithoutRequirements() {
        String excepted = "WHERE 1 = 1 \n" +
                "ORDER BY created_at DESC";

        Sort sort = Sort.by(Sort.Direction.DESC, "created_at");
        String actual = invokeGetSqlQueryMethod(null, sort, null);

        Assertions.assertTrue((actual.toLowerCase()).contains(excepted.toLowerCase()), "excepted: " + excepted);
    }

    @Test
    void getSqlQuery_testWithinRequirements() {
        String excepted = "WHERE 1 = 1 \n" +
                "AND (name LIKE ";


        GetRoutesBrieflyRequest.Requirements req = new GetRoutesBrieflyRequest.Requirements(
                new String[]{"name: ahaha", "two", "three"}, null
        );
        String actual = invokeGetSqlQueryMethod(req, null, null);

        Assertions.assertTrue(actual.contains(excepted), "excepted: " + excepted);
    }

    private void pushRandomTrack(int times) {
        for (int j = 0; j < times; j++) {
            RegistrationResponse userResponse = dataProviderService.addRandomUser();

            RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
                    .name("")
                    .description("")
                    .authorId(userResponse.yourId())
                    .path(new PointDTO[]{
                            new PointDTO(1.0, 2.0, null),
                            new PointDTO(2.0, 3.0, null),
                            new PointDTO(3.0, 4.0, null)
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name1", "desc1", 2.0, 3.0),
                            new KeyPointsDTO("name1", "desc1", 3.0, 4.0)
                    })
                    .build();

            dataProviderService.pushRoute(routeInfoDTO);
        }
    }

    @Test
    @Transactional
    @Rollback
    void findAllTrackWithRequirements_checkEntityCast() {
        pushRandomTrack(1);


        Page<Track> allTrackWithRequirements = trackRepositoryWithDynamicQueryImpl.findAllTrackWithRequirements(null, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, allTrackWithRequirements.getContent().size()),
                () -> Assertions.assertEquals(0, allTrackWithRequirements.getNumber()),
                () -> Assertions.assertEquals(1, allTrackWithRequirements.getTotalPages())
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllTrackWithRequirements_checkManyTrackIsFoundFirstPage() {
        pushRandomTrack(100);


        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Track> allTrackWithRequirements = trackRepositoryWithDynamicQueryImpl.findAllTrackWithRequirements(null, pageRequest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(10, allTrackWithRequirements.getContent().size()),
                () -> Assertions.assertEquals(0, allTrackWithRequirements.getNumber()),
                () -> Assertions.assertEquals(10, allTrackWithRequirements.getTotalPages())
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllTrackWithRequirements_checkManyTrackIsFoundLastPage() {
        pushRandomTrack(105);


        PageRequest pageRequest = PageRequest.of(10, 10);
        Page<Track> allTrackWithRequirements = trackRepositoryWithDynamicQueryImpl.findAllTrackWithRequirements(null, pageRequest);
        Assertions.assertAll(
                () -> Assertions.assertEquals(5, allTrackWithRequirements.getContent().size()),
                () -> Assertions.assertEquals(10, allTrackWithRequirements.getNumber()),
                () -> Assertions.assertEquals(11, allTrackWithRequirements.getTotalPages())
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllTrackWithRequirements_checkContent() {
        RegistrationResponse userResponse = dataProviderService.addRandomUser();
        RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
                .name("name")
                .description("desc")
                .authorId(userResponse.yourId())
                .path(new PointDTO[]{
                        new PointDTO(1.0, 2.0, null),
                        new PointDTO(2.0, 3.0, null),
                        new PointDTO(2.0, 3.0, null),
                        new PointDTO(2.0, 3.0, null),
                        new PointDTO(2.0, 3.0, null),
                        new PointDTO(3.0, 4.0, null)
                })
                .keyPoints(new KeyPointsDTO[]{
                        new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                        new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                        new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                        new KeyPointsDTO("name1", "desc1", 2.0, 3.0),
                        new KeyPointsDTO("name1", "desc1", 3.0, 4.0)
                })
                .build();

        dataProviderService.pushRoute(routeInfoDTO);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Track> allTrackWithRequirements = trackRepositoryWithDynamicQueryImpl.findAllTrackWithRequirements(null, pageRequest);

        Assertions.assertEquals(1, allTrackWithRequirements.getContent().size());
        Assertions.assertAll(
                () -> Assertions.assertEquals(6, allTrackWithRequirements.getContent().get(0).getPoints().size()),
                () -> Assertions.assertEquals(0, allTrackWithRequirements.getContent().get(0).getPoints().get(0).getOrderNumber()),
                () -> Assertions.assertEquals(5, allTrackWithRequirements.getContent().get(0).getKeyPoints().size()),
                () -> Assertions.assertEquals("name1", allTrackWithRequirements.getContent().get(0).getKeyPoints().get(0).getName()),
                () -> Assertions.assertTrue(allTrackWithRequirements.getContent().get(0).getCreatedAt().before(Date.from(Instant.now()))),
                () -> Assertions.assertEquals(userResponse.yourId(), allTrackWithRequirements.getContent().get(0).getCreator().getId()),
                () -> Assertions.assertEquals("name", allTrackWithRequirements.getContent().get(0).getName()),
                () -> Assertions.assertEquals("desc", allTrackWithRequirements.getContent().get(0).getDescription())
        );
    }

    @Test
    @Transactional
    @Rollback
    void findAllTrackWithRequirements_checkKeyPointFilterQuery() {
        RegistrationResponse userResponse = dataProviderService.addRandomUser();

        for (int i = 0; i < 2; i++) {
            RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
                    .name("name")
                    .description("desc")
                    .authorId(userResponse.yourId())
                    .path(new PointDTO[] {
                            new PointDTO(-1d, -1d, null)
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name2", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name3", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name4", "desc1", 2.0, 3.0),
                            new KeyPointsDTO("name5", "desc1", 3.0, 4.0)
                    })
                    .build();
            dataProviderService.pushRoute(routeInfoDTO);
        }

        for (int i = 0; i < 5; i++) {
            RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
                    .name("name")
                    .description("desc")
                    .authorId(userResponse.yourId())
                    .path(new PointDTO[] {
                            new PointDTO(-1d, -1d, null)
                    })
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("name1", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name2", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name-1", "desc1", 1.0, 2.0),
                            new KeyPointsDTO("name4", "desc1", 2.0, 3.0),
                            new KeyPointsDTO("name5", "desc1", 3.0, 4.0)
                    })
                    .build();
            dataProviderService.pushRoute(routeInfoDTO);
        }


        GetRoutesBrieflyRequest request = new GetRoutesBrieflyRequest(0d, 0d,
                new GetRoutesBrieflyRequest.Requirements(new String[]{"key_points: name1<SEP>name3"}, null));
        Page<Track> allTrackWithRequirements = trackRepositoryWithDynamicQueryImpl.findAllTrackWithRequirements(request, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, allTrackWithRequirements.getContent().size()),
                () -> Assertions.assertTrue(allTrackWithRequirements.getContent().get(0).getKeyPoints().stream()
                        .map(KeyPoint::getName).toList().containsAll(List.of("name1", "name3")))
        );
    }
}