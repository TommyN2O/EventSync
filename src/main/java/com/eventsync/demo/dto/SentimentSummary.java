package com.eventsync.demo.dto;

import com.eventsync.demo.model.Sentiment;
import java.util.Map;

public class SentimentSummary {
    private Long eventId;
    private String eventTitle;
    private int totalFeedbacks;
    private Map<Sentiment, Integer> sentimentCounts;
    private Map<Sentiment, Double> sentimentPercentages;
    private Sentiment dominantSentiment;
    
    public SentimentSummary() {}
    
    public SentimentSummary(Long eventId, String eventTitle, int totalFeedbacks, 
                           Map<Sentiment, Integer> sentimentCounts, 
                           Map<Sentiment, Double> sentimentPercentages, 
                           Sentiment dominantSentiment) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.totalFeedbacks = totalFeedbacks;
        this.sentimentCounts = sentimentCounts;
        this.sentimentPercentages = sentimentPercentages;
        this.dominantSentiment = dominantSentiment;
    }
    
    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }
    
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    
    public String getEventTitle() {
        return eventTitle;
    }
    
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    
    public int getTotalFeedbacks() {
        return totalFeedbacks;
    }
    
    public void setTotalFeedbacks(int totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }
    
    public Map<Sentiment, Integer> getSentimentCounts() {
        return sentimentCounts;
    }
    
    public void setSentimentCounts(Map<Sentiment, Integer> sentimentCounts) {
        this.sentimentCounts = sentimentCounts;
    }
    
    public Map<Sentiment, Double> getSentimentPercentages() {
        return sentimentPercentages;
    }
    
    public void setSentimentPercentages(Map<Sentiment, Double> sentimentPercentages) {
        this.sentimentPercentages = sentimentPercentages;
    }
    
    public Sentiment getDominantSentiment() {
        return dominantSentiment;
    }
    
    public void setDominantSentiment(Sentiment dominantSentiment) {
        this.dominantSentiment = dominantSentiment;
    }
} 