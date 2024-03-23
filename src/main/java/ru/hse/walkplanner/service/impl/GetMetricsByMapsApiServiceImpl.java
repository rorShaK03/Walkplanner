package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.client.MapsClient;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.GetMetricsByMapsApiService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class GetMetricsByMapsApiServiceImpl implements GetMetricsByMapsApiService {

    private MapsClient mapsClient;

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Transactional
    @Override
    public void updateDistanceAndTime(Track track) {
        executorService.submit(() -> {
            Integer distanceMeters = mapsClient.getDistanceMeters(track);
            Integer walkMinutes = mapsClient.getWalkMinutes(track);

            track.setDistanceMeters(distanceMeters);
            track.setWalkMinutes(walkMinutes);
        });
    }
}
