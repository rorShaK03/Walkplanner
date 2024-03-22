package ru.hse.walkplanner.service.sorts;

import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;

import java.util.Optional;

public interface SortSpecService {

    Optional<Specification<Track>> getSpec(String sort, InfoFromRequirements info);
}
