package ru.hse.walkplanner.service;

import java.util.Optional;

public interface FilterParserService {

    Optional<String> getSqlInjection(String filter, String[] info);
}
