package ru.hse.walkplanner.service;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface ApplyAnySortingService {

    Optional<String> applyAnySort(Sort.Order order, String[] info);
}
