package ru.hse.walkplanner.client.impl;

import org.springframework.stereotype.Service;
import ru.hse.walkplanner.client.MapsClient;
import ru.hse.walkplanner.entity.Track;

import java.util.Random;

@Service
public class MapsClientMockImpl implements MapsClient {
    @Override
    public Integer getDistanceMeters(Track track) {
        return new Random().nextInt(100, 150) * track.getPoints().size();
    }

    @Override
    public Integer getWalkMinutes(Track track) {
        return new Random().nextInt(2, 8) * track.getPoints().size();
    }
}
