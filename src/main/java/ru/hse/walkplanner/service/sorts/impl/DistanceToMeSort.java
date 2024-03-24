package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.sorts.SortSpecService;

@Component
public class DistanceToMeSort extends AbstractSortParser implements SortSpecService {

    public static final String sortName = "distance_to_me";


    @Override
    protected Specification<Track> getTrackSpecification(String sort, InfoFromRequirements info) {
        Specification<Track> spec;
        if (getDirection(sort).equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> query
                    .where(builder.equal(root.get("points").get("orderNumber"), 0))
                    .orderBy(builder.desc(builder.function("calculate_distance", Float.class,
                            root.get("points").get("latitude"),
                            root.get("points").get("latitude"),
                            builder.literal(info.latitude()),
                            builder.literal(info.longitude())))
                    )
                    .getRestriction();
        } else if (getDirection(sort).equalsIgnoreCase(Sort.Direction.ASC.name())) {
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
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "unknown direction. Excepted 'desc' or 'asc'");
        }
        return spec;
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
