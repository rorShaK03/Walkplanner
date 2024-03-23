package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.Optional;

public abstract class AbstractSortParser implements SortSpecService {
    @Override
    public Optional<Specification<Track>> getSpec(String sort, InfoFromRequirements info) {
        if (!sort.split(",")[0].equalsIgnoreCase(getSortName())) {
            return Optional.empty();
        }

        Specification<Track> spec;
        if (getDirection(sort).equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> (query.orderBy(builder.desc(root.get(getColumnName()))).getRestriction());
        } else if (getDirection(sort).equalsIgnoreCase(Sort.Direction.ASC.name())) {
            spec = (root, query, builder) -> (query.orderBy(builder.asc(root.get(getColumnName()))).getRestriction());
        } else {
            throw new RuntimeException("unknown direction");
        }

        return Optional.of(spec);
    }


    protected abstract String getSortName();

    protected abstract String getColumnName();

    private String getDirection(String sort) {
        return sort.split(",")[1];
    }
}
