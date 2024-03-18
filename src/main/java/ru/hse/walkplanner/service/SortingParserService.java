package ru.hse.walkplanner.service;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface SortingParserService {

    Optional<String> getSqlInjection(Sort.Order order, String[] info);
}
