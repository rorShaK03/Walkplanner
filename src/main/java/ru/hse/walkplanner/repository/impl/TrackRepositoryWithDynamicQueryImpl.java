package ru.hse.walkplanner.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.walkplanner.dto.GetRoutesBrieflyRequest;
import ru.hse.walkplanner.entity.Track;
import ru.hse.walkplanner.repository.TrackRepositoryWithDynamicQuery;
import ru.hse.walkplanner.repository.impl.util.InfoFromRequirements;
import ru.hse.walkplanner.service.ApplyAnyFilterService;
import ru.hse.walkplanner.service.ApplyAnySortingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TrackRepositoryWithDynamicQueryImpl implements TrackRepositoryWithDynamicQuery {

    @PersistenceContext
    private EntityManager entityManager;

    private ApplyAnyFilterService applyAnyFilterService;
    private ApplyAnySortingService applyAnySortingService;

    private final Pageable defaultPageable = PageRequest.of(0, 10, Sort.Direction.DESC, "created_at");

    @Override
    @Transactional
    public Page<Track> findAllTrackWithRequirements(GetRoutesBrieflyRequest request, Pageable pageable) {
        pageable = complementPageable(pageable);
        InfoFromRequirements locationInfo = getLocationInfo(request);
        GetRoutesBrieflyRequest.Requirements requirements = Objects.isNull(request) ? null : request.requirements();

        String sql = getSqlQuery(requirements, pageable.getSort(), locationInfo);

        Query query = entityManager.createNativeQuery(sql, Track.class);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<Track> list = query.getResultList();

        Query queryTotalSize = entityManager.createNativeQuery(getTotalSize(requirements, locationInfo), Integer.class);
        @SuppressWarnings("unchecked")
        int val = ((List<Integer>) queryTotalSize.getResultList()).get(0);

        return new PageImpl<>(list, pageable, val);
    }

    private String getSqlQuery(GetRoutesBrieflyRequest.Requirements requirements, Sort sort, InfoFromRequirements info) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM tracks \n");

        queryBuilder.append("WHERE 1 = 1 \n");
        queryBuilder.append(appendFilters(requirements, info));
        queryBuilder.append(appendSorting(sort, info));


        return queryBuilder.toString();
    }

    private String getTotalSize(GetRoutesBrieflyRequest.Requirements requirements, InfoFromRequirements info) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT count(*) FROM tracks \n");

        queryBuilder.append("WHERE 1 = 1 \n");
        queryBuilder.append(appendFilters(requirements, info));

        return queryBuilder.toString();
    }


    private String appendFilters(GetRoutesBrieflyRequest.Requirements requirements, InfoFromRequirements info) {
        StringBuilder queryBuilder = new StringBuilder();
        if (Objects.nonNull(requirements) && Objects.nonNull(requirements.filter())) {
            for (String filter : requirements.filter()) {
                Optional<String> res = applyAnyFilterService.applyAnyFilter(filter, info);
                if (res.isPresent()) {
                    queryBuilder.append("AND ");
                    queryBuilder.append(res.get());
                    queryBuilder.append(" \n");
                }
            }
        }
        return queryBuilder.toString();
    }

    private String appendSorting(Sort sort, InfoFromRequirements info) {
        StringBuilder queryBuilder = new StringBuilder();
        if (Objects.nonNull(sort)) {
            List<String> orders = new ArrayList<>();

            for (Sort.Order order : sort.get().toList()) {
                Optional<String> res = applyAnySortingService.applyAnySort(order, info);
                res.ifPresent(orders::add);
            }
            if (!orders.isEmpty()) {
                queryBuilder.append("ORDER BY ");
                queryBuilder.append(String.join(" AND ", orders));
            }
        }
        return queryBuilder.toString();
    }


    private Pageable complementPageable(Pageable given) {
        if (Objects.isNull(given)) {
            return defaultPageable;
        }
        if (given.getSort().isEmpty()) {
            return PageRequest.of(given.getPageNumber(), given.getPageSize(), defaultPageable.getSort());
        }
        return given;
    }

    private InfoFromRequirements getLocationInfo(GetRoutesBrieflyRequest request) {
        if (Objects.nonNull(request)) {
            return new InfoFromRequirements(request.latitude(), request.longitude());
        }
        return null;
    }
}
