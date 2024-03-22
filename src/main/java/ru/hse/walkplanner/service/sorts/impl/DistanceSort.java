package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.sorts.SortSpecService;

@Component
public class DistanceSort extends AbstractSortParser implements SortSpecService {

    public static final String sortName = "distance";
    public static final String columnName = "distanceMeters";

    @Override
    protected String getSortName() {
        return sortName;
    }

    @Override
    protected String getColumnName() {
        return columnName;
    }
}
