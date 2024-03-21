package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.FilterParserService;

@Component
public class DistanceToMeMaxFilter extends AbstractFilterParser implements FilterParserService {

    private static final String filterName = "distance_to_me_max";

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String logic(String remain, InfoFromRequirements info) {
        String latitude, longitude;

        try {
            latitude = String.valueOf(info.latitude());
            longitude = String.valueOf(info.longitude());
        } catch (NullPointerException ex) {
            throw new RuntimeException();
        }
        String innerSql = "SELECT tp.track_id\n" +
                "FROM tracks_points tp\n" +
                "INNER JOIN points p\n" +
                "ON tp.points_id = p.id\n" +
                "WHERE p.order_number = 0 AND calculate_distance(p.latitude, p.longitude, " +
                "cast(" + latitude + " as float), " +
                "cast(" + longitude + " as float)) < " + remain + " \n" +
                "GROUP BY tp.track_id\n";

        String sql = "id IN (" + innerSql + ")";
        sql = "(" + sql + ")";
        return sql;
    }
}
