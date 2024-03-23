package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.service.filters.FilterSpecService;

import java.util.ArrayList;
import java.util.List;

@Component
public class DescriptionFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "description";
    private static final String separator = "<SEP>";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        String[] split = remain.split(separator);
        List<Specification<Track>> specsList = new ArrayList<>();

        for (String s : split) {
            Specification<Track> spec = (root, query, builder) -> builder.like(root.get("description"), "%" + s + "%");
            specsList.add(spec);
        }

        return Specification.allOf(specsList);
    }
}
