package com.eventsync.demo.controller;

import com.eventsync.demo.dto.EventRequest;
import com.eventsync.demo.dto.EventResponse;
import com.eventsync.demo.dto.EventCreateRequest;
import com.eventsync.demo.dto.FeedbackRequest;
import com.eventsync.demo.dto.SentimentSummary;
import com.eventsync.demo.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {
    
    private final EventService eventService;
    
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/events/upload")
    public ResponseEntity<EventResponse> createEventWithPhoto(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        
        EventCreateRequest request = new EventCreateRequest(title, description, photo);
        EventResponse response = eventService.createEventWithPhoto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long eventId) {
        EventResponse event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }
    
    @PostMapping("/events/{eventId}/feedback")
    public ResponseEntity<Void> submitFeedback(@PathVariable Long eventId, 
                                              @Valid @RequestBody FeedbackRequest request) {
        eventService.submitFeedback(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @GetMapping("/events/{eventId}/summary")
    public ResponseEntity<SentimentSummary> getEventSentimentSummary(@PathVariable Long eventId) {
        SentimentSummary summary = eventService.getEventSentimentSummary(eventId);
        return ResponseEntity.ok(summary);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File size too large. Please upload an image smaller than 10MB.");
    }
} 