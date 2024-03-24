package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.ApplyAnyFilterSpecService;
import ru.hse.walkplanner.service.filters.FilterSpecService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAnyFilterSpecServiceImpl implements ApplyAnyFilterSpecService {

    private List<FilterSpecService> filters;

    @Override
    public Optional<Specification<Track>> applyAnyFilter(String filter, InfoFromRequirements info) {
        for (FilterSpecService f : filters) {
            try {
                Optional<Specification<Track>> sqlInjection = f.getSpec(filter, info);
                if (sqlInjection.isPresent()) {
                    return sqlInjection;
                }
            } catch (ClientErrorException ex) {
                throw ex;
            } catch (Throwable ignored) {
            }
        }
        throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "Filter " + filter + " not matched with anyone. This filter is not implemented yet");
    }
}
