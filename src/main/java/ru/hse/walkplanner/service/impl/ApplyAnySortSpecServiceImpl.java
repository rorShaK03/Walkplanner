package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.ApplyAnySortSpecService;
import ru.hse.walkplanner.service.sorts.SortSpecService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAnySortSpecServiceImpl implements ApplyAnySortSpecService {

    private List<SortSpecService> sorts;

    @Override
    public Optional<Specification<Track>> applyAnySort(String sort, InfoFromRequirements info) {
        for (SortSpecService s : sorts) {
            try {
                Optional<Specification<Track>> sqlInjection = s.getSpec(sort, info);
                if (sqlInjection.isPresent()) {
                    return sqlInjection;
                }
            } catch (ClientErrorException ex) {
                throw ex;
            } catch (Throwable ignored) {
            }
        }
        throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "Sorting " + sort + " not matched with anyone. This sort is not implemented yet");
    }
}
