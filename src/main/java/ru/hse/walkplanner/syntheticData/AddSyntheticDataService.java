package ru.hse.walkplanner.syntheticData;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.dto.KeyPointsDTO;
import ru.hse.walkplanner.dto.PointDTO;
import ru.hse.walkplanner.dto.RegistrationResponse;
import ru.hse.walkplanner.dto.RoutePushingInfoDTO;
import ru.hse.walkplanner.entity.User;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.repository.UserRepository;
import ru.hse.walkplanner.service.DataProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class AddSyntheticDataService {

    private DataProviderService dataProviderService;

    private TrackRepository trackRepository;
    private UserRepository userRepository;

    private final Double latMoscow = 55.75;
    private final Double longMoscow = 37.62;

    @PostConstruct
    private void fillData() {
        RegistrationResponse response = dataProviderService.addRandomUser();
        User user = userRepository.findById(response.yourId()).get();

        for (int i = 0; i < 100; ++i) {

            List<PointDTO> pointDTOList = new ArrayList<>();

            double currentLat = latMoscow, currentLong = longMoscow;
            for (int k = 0; k < new Random().nextInt(33, 77); ++k) {
                double offsetLat = new Random().nextDouble(-0.01, 0.01);
                double offsetLong = new Random().nextDouble(-0.01, 0.01);

                currentLat += offsetLat;
                currentLong += offsetLong;
                pointDTOList.add(new PointDTO(currentLat, currentLong, null));
            }

            RoutePushingInfoDTO routeInfoDTO = RoutePushingInfoDTO.builder()
                    .name("route#" + i)
                    .description("description for route#" + i)
                    .authorId(user.getId())
                    .path(pointDTOList.toArray(PointDTO[]::new))
                    .keyPoints(new KeyPointsDTO[]{
                            new KeyPointsDTO("name1", "desc", 1d, 1d),
                            new KeyPointsDTO("name2", "desc", 1d, 1d),
                            new KeyPointsDTO("name3", "desc", 1d, 1d)
                    })
                    .build();

            dataProviderService.pushRoute(routeInfoDTO);
        }
    }
}
