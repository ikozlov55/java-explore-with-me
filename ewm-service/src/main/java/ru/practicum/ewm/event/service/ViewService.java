package ru.practicum.ewm.event.service;

public interface ViewService {
    long getEventViews(long eventId);

    void eventViewHit(long eventId, String ip);

    void eventListView(String ip);
}
