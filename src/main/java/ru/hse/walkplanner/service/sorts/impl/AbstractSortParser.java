package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.Optional;

public abstract class AbstractSortParser implements SortSpecService {
    @Override
    public Optional<Specification<Track>> getSpec(String sort, InfoFromRequirements info) {
        try {
            if (!sort.split(",")[0].equalsIgnoreCase(getSortName())) {
                return Optional.empty();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "bad passed value (can not split by ',' to find property). Excepted smth like 'created_at,desc', but was " + sort);
        }

        Specification<Track> spec = getTrackSpecification(sort, info);
        return Optional.of(spec);
    }

    protected Specification<Track> getTrackSpecification(String sort, InfoFromRequirements unused) {
        Specification<Track> spec;
        if (getDirection(sort).equalsIgnoreCase(Sort.Direction.DESC.name())) {
            spec = (root, query, builder) -> (query.orderBy(builder.desc(root.get(getColumnName()))).getRestriction());
        } else if (getDirection(sort).equalsIgnoreCase(Sort.Direction.ASC.name())) {
            spec = (root, query, builder) -> (query.orderBy(builder.asc(root.get(getColumnName()))).getRestriction());
        } else {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "unknown direction. Excepted 'desc' or 'asc'");
        }
        return spec;
    }


    protected abstract String getSortName();

    protected abstract String getColumnName();

    protected String getDirection(String sort) {
        try {
            return sort.split(",")[1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "bad passed value (can not split by ',' to find direction). Excepted smth like 'created_at,desc', but was " + sort);
        }
    }
}
