package ru.hse.walkplanner.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.entity.Track;

public interface ApplyAllFilterSpecService {

    Specification<Track> getQuerySpecification(GetRoutesBrieflyRequest request, String sort);
}
