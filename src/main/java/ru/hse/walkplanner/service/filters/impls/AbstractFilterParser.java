package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.filters.FilterSpecService;

import java.util.Optional;

public abstract class AbstractFilterParser implements FilterSpecService {

    @Override
    public Optional<Specification<Track>> getSpec(String filter, InfoFromRequirements info) {
        if (!filter.startsWith(getFilterName())) {
            return Optional.empty();
        }

        String body = filter.substring(getFilterName().length() + 1).strip();
        if (body.isEmpty()) {
            throw new ClientErrorException(HttpStatus.NO_CONTENT.value(), "body not passed");
        }
        return Optional.of(logic(body, info));
    }

    public abstract String getFilterName();

    public abstract Specification<Track> logic(String remain, InfoFromRequirements info);
}
