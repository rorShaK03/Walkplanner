package ru.hse.walkplanner.service.filtersImpl;

import org.springframework.stereotype.Component;
import ru.hse.walkplanner.service.FilterParserService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class KeyPointsFilter implements FilterParserService {

    private static final String filterName = "key_points";
    private static final String separator = "<SEP>";

    @Override
    public Optional<String> getSqlInjection(String filter, String[] unused) {
        try {
            if (!filter.startsWith(filterName)) {
                return Optional.empty();
            }


            String remain = filter.substring(filterName.length() + 1).strip();
            String[] split = remain.split(separator);
            split = Arrays.stream(split).map(it -> "'" + it + "'").toArray(String[]::new);

            String innerSql = "SELECT tk.track_id\n" +
                    "FROM tracks_key_points tk\n" +
                    "INNER JOIN key_points k\n" +
                    "ON tk.key_points_id = k.id\n" +
                    "WHERE k.name IN (" + String.join(", ", split) + ")\n" +
                    "GROUP BY tk.track_id\n" +
                    "HAVING COUNT(DISTINCT tk.key_points_id) = " + split.length;

            String sql = "id IN (" + innerSql + ")";
            sql = "(" + sql + ")";
            return Optional.of(sql);
        } catch (Throwable ignored) {
            return Optional.empty();
        }
    }
}
