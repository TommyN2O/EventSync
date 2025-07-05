package com.eventsync.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class EventCreateRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    private MultipartFile photo;
    
    public EventCreateRequest() {}
    
    public EventCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
    
    public EventCreateRequest(String title, String description, MultipartFile photo) {
        this.title = title;
        this.description = description;
        this.photo = photo;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public MultipartFile getPhoto() {
        return photo;
    }
    
    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
} 