package ru.hse.walkplanner.client;

import ru.hse.walkplanner.entity.Track;

public interface MapsClient {

    Integer getDistanceMeters(Track track);
    Integer getWalkMinutes(Track track);
}
