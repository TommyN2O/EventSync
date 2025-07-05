package com.eventsync.demo.service;

import com.eventsync.demo.dto.EventRequest;
import com.eventsync.demo.dto.EventResponse;
import com.eventsync.demo.dto.EventCreateRequest;
import com.eventsync.demo.dto.FeedbackRequest;
import com.eventsync.demo.dto.SentimentSummary;
import com.eventsync.demo.model.Event;
import com.eventsync.demo.model.Feedback;
import com.eventsync.demo.model.Sentiment;
import com.eventsync.demo.repository.EventRepository;
import com.eventsync.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final FileUploadService fileUploadService;
    
    @Autowired
    public EventServiceImpl(EventRepository eventRepository, 
                           FeedbackRepository feedbackRepository,
                           SentimentAnalysisService sentimentAnalysisService,
                           FileUploadService fileUploadService) {
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
        this.sentimentAnalysisService = sentimentAnalysisService;
        this.fileUploadService = fileUploadService;
    }
    
    @Override
    public EventResponse createEvent(EventRequest request) {
        Event event = new Event(request.getTitle(), request.getDescription(), request.getPhotoUrl());
        Event savedEvent = eventRepository.save(event);
        return convertToEventResponse(savedEvent);
    }
    
    @Override
    public EventResponse createEventWithPhoto(EventCreateRequest request) {
        String photoPath = null;
        
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            try {
                photoPath = fileUploadService.uploadImage(request.getPhoto());
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }
        
        Event event = new Event(request.getTitle(), request.getDescription(), photoPath);
        Event savedEvent = eventRepository.save(event);
        return convertToEventResponse(savedEvent);
    }
    
    @Override
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::convertToEventResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public EventResponse getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return convertToEventResponse(event);
    }
    
    @Override
    public void submitFeedback(Long eventId, FeedbackRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        Feedback feedback = new Feedback(request.getText(), event);
        
        // Analyze sentiment
        Sentiment sentiment = sentimentAnalysisService.analyzeSentiment(request.getText());
        
        feedback.setSentiment(sentiment);
        
        feedbackRepository.save(feedback);
    }
    
    @Override
    public SentimentSummary getEventSentimentSummary(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        
        long totalFeedbacks = feedbackRepository.countByEventId(eventId);
        
        Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
        Map<Sentiment, Double> sentimentPercentages = new HashMap<>();
        
        for (Sentiment sentiment : Sentiment.values()) {
            long count = feedbackRepository.countByEventIdAndSentiment(eventId, sentiment);
            sentimentCounts.put(sentiment, (int) count);
            
            double percentage = totalFeedbacks > 0 ? (double) count / totalFeedbacks * 100 : 0.0;
            sentimentPercentages.put(sentiment, percentage);
        }
        
        // Calculate dominant sentiment
        Sentiment dominantSentiment = Sentiment.NEUTRAL; // Default
        int maxCount = 0;
        
        for (Sentiment sentiment : Sentiment.values()) {
            int count = sentimentCounts.get(sentiment);
            if (count > maxCount) {
                maxCount = count;
                dominantSentiment = sentiment;
            }
        }
        
        return new SentimentSummary(
                eventId,
                event.getTitle(),
                (int) totalFeedbacks,
                sentimentCounts,
                sentimentPercentages,
                dominantSentiment
        );
    }
    
    private EventResponse convertToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getPhotoUrl(),
                event.getCreatedAt()
        );
    }
} 