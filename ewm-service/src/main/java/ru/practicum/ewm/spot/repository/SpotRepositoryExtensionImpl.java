package ru.practicum.ewm.spot.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.spot.model.Spot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpotRepositoryExtensionImpl implements SpotRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Spot findByIdOrThrow(long spotId) {
        return entityManager.createQuery("SELECT s FROM Spot s WHERE s.id = :spotId", Spot.class)
                .setParameter("spotId", spotId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Spot with id=%d was not found", spotId);
                    return new NotFoundException(msg);
                });
    }

    @Override
    public Collection<Spot> getSpots(String text, int from, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Spot> query = builder.createQuery(Spot.class);
        Root<Spot> root = query.from(Spot.class);
        List<Predicate> predicates = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            String like = String.format("%%%s%%", text.toLowerCase());
            Predicate titleLike = builder.like(builder.lower(root.get("title")), like);
            Predicate descriptionLike = builder.like(builder.lower(root.get("description")), like);
            predicates.add(builder.or(titleLike, descriptionLike));
        }
        query.where(predicates.toArray(Predicate[]::new));

        TypedQuery<Spot> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }
}
