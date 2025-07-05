package com.eventsync.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackRequest {
    
    @NotBlank(message = "Feedback text is required")
    @Size(max = 1000, message = "Feedback text must be less than 1000 characters")
    private String text;
    
    public FeedbackRequest() {}
    
    public FeedbackRequest(String text) {
        this.text = text;
    }
    
    // Getters and Setters
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
} 