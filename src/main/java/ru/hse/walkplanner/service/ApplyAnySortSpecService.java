package ru.hse.walkplanner.service;

import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;

import java.util.Optional;

public interface ApplyAnySortSpecService {

    Optional<Specification<Track>> applyAnySort(String sort, InfoFromRequirements info);
}
