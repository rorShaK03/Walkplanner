package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.ApplyAnySortingService;
import ru.hse.walkplanner.service.SortingParserService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAnySortingServiceImpl implements ApplyAnySortingService {

    List<SortingParserService> sortingParserServices;

    @Override
    public Optional<String> applyAnySort(Sort.Order order, InfoFromRequirements info) {
        for (SortingParserService s : sortingParserServices) {
            try {
                Optional<String> sqlInjection = s.getSqlInjection(order, info);
                if (sqlInjection.isPresent()) {
                    return sqlInjection;
                }
            } catch (RuntimeException ex) {
                throw new RuntimeException(ex);
            } catch (Throwable ignored) {
            }
        }
        throw new RuntimeException("No matched any sort, but was passed " + order.getProperty() + " " + order.getDirection());
    }
}
