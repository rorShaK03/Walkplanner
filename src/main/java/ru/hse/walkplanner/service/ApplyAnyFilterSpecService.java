package ru.hse.walkplanner.service;

import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;

import java.util.Optional;

public interface ApplyAnyFilterSpecService {

    Optional<Specification<Track>> applyAnyFilter(String filter, InfoFromRequirements info);
}
