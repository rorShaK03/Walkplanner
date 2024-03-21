package ru.hse.walkplanner.service;

import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;

import java.util.Optional;

public interface FilterParserService {

    Optional<String> getSqlInjection(String filter, InfoFromRequirements info);
}
