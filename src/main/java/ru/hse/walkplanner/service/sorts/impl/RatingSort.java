package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.Optional;

@Component
public class RatingSort extends AbstractSortParser implements SortSpecService {

    public static final String sortName = "rating";

    @Override
    protected Specification<Track> getTrackSpecification(String sort, InfoFromRequirements unused) {
        Specification<Track> specNonZero =
                (root, query, builder) -> builder.greaterThan(root.get("ratedUsers").as(Integer.class), 0);

        Specification<Track> spec;
        if (getDirection(sort).equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> query
                    .orderBy(builder.desc(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers"))))
                    .getRestriction();
        } else if (getDirection(sort).equalsIgnoreCase(Sort.Direction.ASC.name())) {
            spec = (root, query, builder) -> query
                    .orderBy(builder.asc(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers"))))
                    .getRestriction();
        } else {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "unknown direction. Excepted 'desc' or 'asc'");
        }

        return Specification.allOf(specNonZero, spec);
    }

    @Override
    protected String getSortName() {
        return sortName;
    }

    @Override
    protected String getColumnName() {
        return null;
    }
}
