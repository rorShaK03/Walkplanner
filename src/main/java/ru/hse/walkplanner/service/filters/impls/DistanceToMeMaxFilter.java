package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class DistanceToMeMaxFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "distance_to_me_max";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements info) {
        float val = Float.parseFloat(remain);

        return (root, query, builder) -> builder.and(
                builder.equal(root.get("points").get("orderNumber"), 0),
                builder.lessThan(
                        builder.function("calculate_distance", Float.class,
                                root.get("points").get("latitude"),
                                root.get("points").get("latitude"),
                                builder.literal(info.latitude()),
                                builder.literal(info.longitude())),
                        builder.literal(val)
                )
        );
    }
}
