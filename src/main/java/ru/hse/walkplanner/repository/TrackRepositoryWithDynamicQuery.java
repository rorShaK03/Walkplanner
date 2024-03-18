package ru.hse.walkplanner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.entity.Track;

public interface TrackRepositoryWithDynamicQuery {
    @Transactional
    Page<Track> findAllTrackWithRequirements(GetRoutesBrieflyRequest request, Pageable pageable);
}
