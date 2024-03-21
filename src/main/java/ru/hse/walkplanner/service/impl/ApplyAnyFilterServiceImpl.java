package ru.hse.walkplanner.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.ApplyAnyFilterService;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplyAnyFilterServiceImpl implements ApplyAnyFilterService {

    List<FilterParserService> filterParserServices;

    @Override
    public Optional<String> applyAnyFilter(String filter, InfoFromRequirements info) {
        for (FilterParserService f : filterParserServices) {
            try {
                Optional<String> sqlInjection = f.getSqlInjection(filter, info);
                if (sqlInjection.isPresent()) {
                    return sqlInjection;
                }
            } catch (Throwable ignored) {
            }
        }
        return Optional.empty();
    }
}
