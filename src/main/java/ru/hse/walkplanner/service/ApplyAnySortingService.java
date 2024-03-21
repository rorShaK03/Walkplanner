package ru.hse.walkplanner.service;

import org.springframework.data.domain.Sort;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;

import java.util.Optional;

public interface ApplyAnySortingService {

    Optional<String> applyAnySort(Sort.Order order, InfoFromRequirements info);
}
