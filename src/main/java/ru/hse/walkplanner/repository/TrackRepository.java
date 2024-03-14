package ru.hse.walkplanner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.walkplanner.entity.Track;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {

    Page<Track> findAllByNameInAndDescriptionIn(List<String> name, List<String> description, Pageable pageable);
}
