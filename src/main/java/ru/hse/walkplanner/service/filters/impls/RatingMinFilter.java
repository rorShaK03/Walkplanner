package ru.hse.walkplanner.service.filters.impls;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.hse.walkplanner.dto.util.InfoFromRequirements;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.exception.ClientErrorException;
import ru.hse.walkplanner.service.filters.FilterSpecService;

@Component
public class RatingMinFilter extends AbstractFilterParser implements FilterSpecService {

    private static final String filterName = "rating_min";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public Specification<Track> logic(String remain, InfoFromRequirements unused) {
        float val;
        try {
            val = Float.parseFloat(remain);
        } catch (NumberFormatException ex) {
            throw new ClientErrorException(HttpStatus.BAD_REQUEST.value(), "For filter " + filterName + " you should pass a float value. I cat not parse: " + remain);
        }

        return (root, query, builder) -> builder.and(
                builder.notEqual(root.get("ratedUsers"), 0),
                builder.greaterThan(builder.function("calculate_rating", Float.class, root.get("rating"), root.get("ratedUsers")), val)
        );
    }
}
