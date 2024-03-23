package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class DistanceToMeMinFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "distance_to_me_min";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements info) {
        float val = Float.parseFloat(remain);

        return (root, query, builder) -> builder.and(
                builder.equal(root.get("points").get("orderNumber"), 0),
                builder.greaterThan(
                        builder.function("calculate_distance", Float.class,
                                root.get("points").get("latitude"),
                                root.get("points").get("longitude"),
                                builder.literal(info.latitude()),
                                builder.literal(info.longitude())),
                        builder.literal(val)
                )
        );
    }
}
