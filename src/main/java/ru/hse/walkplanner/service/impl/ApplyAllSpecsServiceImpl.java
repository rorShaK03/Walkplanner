package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.ApplyAllSpecsService;
import ru.hse.walkplanner.service.ApplyAnyFilterSpecService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAllSpecsServiceImpl implements ApplyAllSpecsService {

    private ApplyAnyFilterSpecService applyAnyFilter;
    private ApplyAnySortSpecServiceImpl applyAnySort;

    @Override
    public Specification<Track> getQuerySpecification(GetRoutesBrieflyRequest request, String sort) {
        return Specification.allOf(
                appendFilters(request),
                appendSorts(request, sort)
        );
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

    private Specification<Track> appendSorts(GetRoutesBrieflyRequest request, String sort) {
        InfoFromRequirements info = getLocationInfo(request);

        if (Objects.isNull(sort) || sort.isEmpty()) {
            sort = "created_at,desc";
        }

        Optional<Specification<Track>> trackSpecification = applyAnySort.applyAnySort(sort, info);
        return trackSpecification.orElseGet(() -> Specification.where(null));
    }

    private InfoFromRequirements getLocationInfo(GetRoutesBrieflyRequest request) {
        if (Objects.nonNull(request)) {
            return new InfoFromRequirements(request.latitude(), request.longitude());
        }
        return null;
    }
}
