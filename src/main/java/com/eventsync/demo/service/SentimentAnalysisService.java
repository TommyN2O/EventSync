package com.eventsync.demo.service;

import com.eventsync.demo.model.Sentiment;

public interface SentimentAnalysisService {
    Sentiment analyzeSentiment(String text);
} 