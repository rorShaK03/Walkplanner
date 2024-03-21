package ru.hse.walkplanner.service;

import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;

import java.util.Optional;

public interface ApplyAnyFilterService {

    Optional<String> applyAnyFilter(String filter, InfoFromRequirements info);
}
