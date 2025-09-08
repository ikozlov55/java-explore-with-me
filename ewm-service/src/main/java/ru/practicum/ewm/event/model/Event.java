package ru.practicum.ewm.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SourceType;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String annotation;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "request_moderation")
    private Boolean requestModeration = true;
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;
    @Formula("""
            (SELECT COUNT(p.id)
               FROM participation_requests p
              WHERE p.event_id = id
                AND p.status = 'CONFIRMED')
            """)
    private Long confirmedRequests;
    private Boolean paid = false;
    private Double lat;
    private Double lon;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", nullable  = false, updatable = false)
    private LocalDateTime createdOn;
    @Column(name = "published_at")
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @OneToMany
    @JoinColumn(name = "event_id")
    private List<Request> requests;
    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;
}
