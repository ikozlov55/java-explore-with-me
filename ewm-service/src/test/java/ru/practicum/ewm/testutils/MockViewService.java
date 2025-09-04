package ru.practicum.ewm.testutils;

import ru.practicum.ewm.event.service.ViewService;

import java.util.HashMap;

public class MockViewService implements ViewService {
    private final HashMap<Long, Long> views = new HashMap<>();

    @Override
    public long getEventViews(long eventId) {
        return views.getOrDefault(eventId, 0L);
    }

    @Override
    public void eventViewHit(long eventId, String ip) {
        views.put(eventId, views.getOrDefault(eventId, 0L) + 1);
    }

    @Override
    public void eventListView(String ip) {

    }
}