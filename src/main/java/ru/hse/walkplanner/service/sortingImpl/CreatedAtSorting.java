package ru.hse.walkplanner.service.sortingImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.SortingParserService;

@Component
public class CreatedAtSorting extends AbstractSortingParser implements SortingParserService {

    private static final String filterName = "created_at";

    @Override
    public String getFilterName() {
        return filterName;
    }
}
