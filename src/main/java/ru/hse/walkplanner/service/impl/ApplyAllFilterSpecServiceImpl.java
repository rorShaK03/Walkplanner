package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.service.ApplyAllFilterSpecService;
import ru.hse.walkplanner.service.ApplyAnyFilterSpecService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAllFilterSpecServiceImpl implements ApplyAllFilterSpecService {

    private ApplyAnyFilterSpecService applyAnyFilter;

    @Override
    public Specification<Track> getQuerySpecification(GetRoutesBrieflyRequest request, String sort) {
        Specification<Track> trackSpecification = appendFilters(request);
        return trackSpecification;
    }

    private Specification<Track> appendFilters(GetRoutesBrieflyRequest request) {
        InfoFromRequirements info = getLocationInfo(request);
        GetRoutesBrieflyRequest.Requirements requirements = request.requirements();

        if (Objects.isNull(requirements) || Objects.isNull(requirements.filter())) {
            return Specification.where(null);
        }

        List<Specification<Track>> specs = new ArrayList<>();
        for (String filter : requirements.filter()) {
            Optional<Specification<Track>> res = applyAnyFilter.applyAnyFilter(filter, info);
            res.ifPresent(specs::add);
        }

        return Specification.allOf(specs);
    }

    private InfoFromRequirements getLocationInfo(GetRoutesBrieflyRequest request) {
        if (Objects.nonNull(request)) {
            return new InfoFromRequirements(request.latitude(), request.longitude());
        }
        return null;
    }
}
