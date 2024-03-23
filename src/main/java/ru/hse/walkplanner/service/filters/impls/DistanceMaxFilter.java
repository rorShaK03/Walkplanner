package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class DistanceMaxFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "distance_max";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        Double val = Double.parseDouble(remain);

        return (root, query, builder) -> builder.lessThan(root.get("distanceMeters"), builder.literal(val));
    }
}
