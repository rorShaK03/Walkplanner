package ru.hse.walkplanner.service.filters;

import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;

import java.util.Optional;

public interface FilterSpecService {

    Optional<Specification<Track>> getSpec(String filter, InfoFromRequirements info);
}
