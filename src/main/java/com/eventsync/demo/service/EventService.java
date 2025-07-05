package com.eventsync.demo.service;

import com.eventsync.demo.dto.EventRequest;
import com.eventsync.demo.dto.EventResponse;
import com.eventsync.demo.dto.EventCreateRequest;
import com.eventsync.demo.dto.FeedbackRequest;
import com.eventsync.demo.dto.SentimentSummary;

import java.util.List;

public interface EventService {
    EventResponse createEvent(EventRequest request);
    EventResponse createEventWithPhoto(EventCreateRequest request);
    List<EventResponse> getAllEvents();
    EventResponse getEventById(Long eventId);
    void submitFeedback(Long eventId, FeedbackRequest request);
    SentimentSummary getEventSentimentSummary(Long eventId);
} 