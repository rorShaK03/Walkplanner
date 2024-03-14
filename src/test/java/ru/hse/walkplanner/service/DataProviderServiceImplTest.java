package ru.hse.walkplanner.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hse.walkplanner.entity.Point;
import ru.hse.walkplanner.repository.PointRepository;
import ru.hse.walkplanner.repository.TrackRepository;
import ru.hse.walkplanner.service.impl.DataProviderServiceImpl;
import testContainer.IntegrationEnvironment;

@SpringBootTest
class DataProviderServiceImplTest extends IntegrationEnvironment {

    @Autowired
    private DataProviderServiceImpl dataProviderServiceImpl;

    @Autowired
    private PointRepository pointRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        pointRepository.save(new Point(null, 1d, 2d, 1, null));

        System.out.println(pointRepository.findAll());
    }
}