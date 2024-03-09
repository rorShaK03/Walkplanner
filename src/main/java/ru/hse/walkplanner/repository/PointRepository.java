package ru.hse.walkplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.walkplanner.entity.Point;

import java.util.UUID;

@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {
}
