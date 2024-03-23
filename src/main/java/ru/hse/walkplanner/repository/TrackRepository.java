package ru.hse.walkplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.hse.walkplanner.entity.Track;

import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID>, JpaSpecificationExecutor<Track> {
}
