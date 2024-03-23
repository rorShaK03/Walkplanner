package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class NameFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "name";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + remain + "%");
    }
}
