package ru.hse.walkplanner.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.KeyPointsDTO;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.PushingRouteResponse;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RouteInfoDTO;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.repository.KeyPointRepository;
import ru.hse.walkplanner.repository.PointRepository;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.repository.UserRepository;
import ru.hse.walkplanner.service.impl.DataProviderServiceImpl;
import testContainer.IntegrationEnvironment;

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
        RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
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

    private Track pushRandomTrack() {
        RegistrationResponse user = dataProviderServiceImpl.addRandomUser();
        RouteInfoDTO routeInfoDTO = RouteInfoDTO.builder()
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

        return trackRepository.findById(response.entityId()).get();
    }

    @Test
    @Transactional
    @Rollback
    void getRoutesBriefly_testCorrectLogic() {
        Track track = pushRandomTrack();

//        dataProviderServiceImpl.getRoutesBriefly();
    }
}