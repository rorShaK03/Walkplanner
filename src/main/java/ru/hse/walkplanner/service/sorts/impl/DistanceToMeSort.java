package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.Optional;

@Component
public class DistanceToMeSort implements SortSpecService {

    public static final String sortName = "distance_to_me";

    @Override
    public Optional<Specification<Track>> getSpec(String sort, InfoFromRequirements info) {
        if (!sort.startsWith(sortName)) {
            return Optional.empty();
        }

        String direction = sort.split(",")[1];

        Specification<Track> spec;
        if (direction.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> query
                    .where(builder.equal(root.get("points").get("orderNumber"), 0))
                    .orderBy(builder.desc(builder.function("calculate_distance", Float.class,
                            root.get("points").get("latitude"),
                            root.get("points").get("latitude"),
                            builder.literal(info.latitude()),
                            builder.literal(info.longitude())))
                    )
                    .getRestriction();
        } else if (direction.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            spec = (root, query, builder) -> query
                    .where(builder.equal(root.get("points").get("orderNumber"), 0))
                    .orderBy(builder.asc(builder.function("calculate_distance", Float.class,
                            root.get("points").get("latitude"),
                            root.get("points").get("latitude"),
                            builder.literal(info.latitude()),
                            builder.literal(info.longitude())))
                    )
                    .getRestriction();
        } else {
            throw new RuntimeException("unknown direction");
        }

        return Optional.of(spec);
    }
}
