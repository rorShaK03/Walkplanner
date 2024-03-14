package ru.hse.walkplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.walkplanner.entity.KeyPoint;

import java.util.UUID;

@Repository
public interface KeyPointRepository extends JpaRepository<KeyPoint, UUID> {
}
