package ru.hse.walkplanner.service;

import java.util.Optional;

public interface ApplyAnyFilterService {

    Optional<String> applyAnyFilter(String filter, String[] info);
}
