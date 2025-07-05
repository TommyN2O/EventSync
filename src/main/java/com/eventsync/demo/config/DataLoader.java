package com.eventsync.demo.config;

import com.eventsync.demo.model.Event;
import com.eventsync.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {
    
    private final EventRepository eventRepository;
    
    @Autowired
    public DataLoader(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    @PostConstruct
    public void loadData() {
        try {
            // Load sample events if database is empty
            if (eventRepository.count() == 0) {
                loadSampleEvents();
            }
        } catch (Exception e) {
            System.out.println("Database not ready yet, skipping sample data load: " + e.getMessage());
        }
    }
    
    private void loadSampleEvents() {
        Event event1 = new Event(
                "Spring Boot Workshop",
                "Learn the fundamentals of Spring Boot framework and build RESTful APIs",
                "images/events/spring_boot.jpg"
        );
        
        Event event2 = new Event(
                "AI and Machine Learning Conference",
                "Explore the latest developments in artificial intelligence and machine learning",
                "images/events/ai_conf.jpg"
        );
        
        Event event3 = new Event(
                "Web Development Bootcamp",
                "Intensive course covering modern web development technologies and best practices",
                "images/events/web_development.png"
        );
        
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        
        System.out.println("Sample events loaded successfully!");
    }
} 