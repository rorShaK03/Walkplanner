package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

import java.util.List;

@Component
public class KeyPointsFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "key_points";
    private static final String separator = "<SEP>";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        String[] split = remain.split(separator);
        return (root, query, builder) -> builder.in(root.get("keyPoints").get("name")).value(List.of(split));
    }
}
