package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class RatingMaxFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "rating_max";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        Float val = Float.parseFloat(remain);

        return (root, query, builder) -> builder.and(
                builder.notEqual(root.get("ratedUsers"), 0),
                builder.lessThan(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers")), val)
        );
    }
}
