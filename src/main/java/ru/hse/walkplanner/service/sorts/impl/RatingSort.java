package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.Optional;

@Component
public class RatingSort implements SortSpecService {

    public static final String sortName = "rating";

    @Override
    public Optional<Specification<Track>> getSpec(String sort, InfoFromRequirements info) {
        if (!sort.startsWith(sortName)) {
            return Optional.empty();
        }

        String direction = sort.split(",")[1];

        Specification<Track> specNonZero =
                (root, query, builder) -> builder.greaterThan(root.get("ratedUsers").as(Integer.class), 0);

        Specification<Track> spec;
        if (direction.equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> query
                    .orderBy(builder.desc(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers"))))
                    .getRestriction();
        } else if (direction.equalsIgnoreCase(Sort.Direction.ASC.name())) {
            spec = (root, query, builder) -> query
                    .orderBy(builder.asc(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers"))))
                    .getRestriction();
        } else {
            throw new RuntimeException("unknown direction");
        }

        return Optional.of(Specification.allOf(specNonZero, spec));
    }
}
