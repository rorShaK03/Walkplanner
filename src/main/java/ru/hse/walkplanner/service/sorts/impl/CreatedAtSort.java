package ru.hse.walkplanner.service.sorts.impl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.sorts.SortSpecService;

@Component
public class CreatedAtSort extends AbstractSortParser implements SortSpecService {

    public static final String sortName = "created_at";
    public static final String columnName = "createdAt";

    @Override
    protected String getSortName() {
        return sortName;
    }

    @Override
    protected String getColumnName() {
        return columnName;
    }
}
