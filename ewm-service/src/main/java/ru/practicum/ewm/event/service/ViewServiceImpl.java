package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.StatViewDto;
import ru.practicum.statsclient.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Import(StatsClient.class)
public class ViewServiceImpl implements ViewService {
    private static final String SERVICE_NAME = "ewm-service";
    private static final String EVENT_LIST_VIEW_URI = "/events";
    private static final String EVENT_VIEW_URI = "/events/%d";
    private static final int VIEW_DAYS_INTERVAL = 30;
    private final StatsClient statsClient;

    @Override
    public long getEventViews(long eventId) {
        List<StatViewDto> views = statsClient.getStats(LocalDateTime.now().minusDays(VIEW_DAYS_INTERVAL),
                LocalDateTime.now().plusDays(VIEW_DAYS_INTERVAL),
                List.of(String.format(EVENT_VIEW_URI, eventId)),
                true).getBody();
        return views == null || views.isEmpty() ? 0 : views.stream().mapToLong(StatViewDto::hits).sum();
    }

    @Override
    public void eventViewHit(long eventId, String ip) {
        EndpointHitDto hit = new EndpointHitDto(SERVICE_NAME,
                String.format(EVENT_VIEW_URI, eventId),
                ip,
                LocalDateTime.now());
        statsClient.saveEndpointHit(hit);
    }

    @Override
    public void eventListView(String ip) {
        EndpointHitDto hit = new EndpointHitDto(SERVICE_NAME,
                EVENT_LIST_VIEW_URI,
                ip,
                LocalDateTime.now());
        statsClient.saveEndpointHit(hit);
    }
}
