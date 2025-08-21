package ru.practicum.ewm.stats.service.hit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import ru.practicum.ewm.stats.dto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

public class EndpointHitRepositoryExtensionImpl implements EndpointHitRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StatViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StatViewDto> query = builder.createQuery(StatViewDto.class);
        Root<EndpointHit> root = query.from(EndpointHit.class);
        Expression<Long> hits = unique ? builder.countDistinct(root.get("ip")) : builder.count(root);
        query.multiselect(root.get("app"), root.get("uri"), hits)
                .where(builder.between(root.get("timestamp"), start, end))
                .groupBy(root.get("app"), root.get("uri"))
                .orderBy(builder.desc(hits));
        if (uris != null && !uris.isEmpty()) {
            query.where(root.get("uri").in(uris));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
