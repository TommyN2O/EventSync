package com.eventsync.demo.repository;

import com.eventsync.demo.model.Feedback;
import com.eventsync.demo.model.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.event.id = :eventId")
    long countByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.event.id = :eventId AND f.sentiment = :sentiment")
    long countByEventIdAndSentiment(@Param("eventId") Long eventId, @Param("sentiment") Sentiment sentiment);
} 