package ru.hse.walkplanner.service.filtersImpl;

import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Optional;

public abstract class AbstractFilterParser implements FilterParserService {

    @Override
    public Optional<String> getSqlInjection(String filter, InfoFromRequirements info) {
        try {
            if (!filter.startsWith(getFilterName())) {
                return Optional.empty();
            }
        } catch (Throwable ignored) {
            return Optional.empty();
        }


        String body = filter.substring(getFilterName().length() + 1).strip();
        if (body.isEmpty()) {
            throw new RuntimeException("body not passed");
        }
        return Optional.of(logic(body, info));
    }

    public abstract String getFilterName();

    public abstract String logic(String remain, InfoFromRequirements info);
}
