package com.eventsync.demo.service;

import com.eventsync.demo.model.Sentiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceSentimentAnalysisService implements SentimentAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceSentimentAnalysisService.class);
    
    private final WebClient webClient;
    private final String apiUrl;
    private final String apiToken;
    
    public HuggingFaceSentimentAnalysisService(
            @Value("${huggingface.api.url}") String apiUrl,
            @Value("${huggingface.api.token}") String apiToken) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
        this.webClient = WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + apiToken)
                .build();
    }
    
    @Override
    public Sentiment analyzeSentiment(String text) {
        try {
            // Real Hugging Face API call
            Map<String, Object> requestBody = Map.of("inputs", text);
            
            logger.info("Sending text to Hugging Face API: {}", text);
            
            List<Object> response = webClient.post()
                    .uri(apiUrl)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            
            logger.info("Full Hugging Face API response: {}", response);
            
            if (response != null && !response.isEmpty()) {
                // The response is a nested array: [[{label=1 star, score=0.8}, {label=2 stars, score=0.15}, ...]]
                Object firstElement = response.get(0);
                logger.info("First element: {}", firstElement);
                
                if (firstElement instanceof List) {
                    List<Map<String, Object>> labelScorePairs = (List<Map<String, Object>>) firstElement;
                    logger.info("Label-score pairs: {}", labelScorePairs);
                    
                    // Find the highest scoring label
                    Map<String, Object> bestMatch = null;
                    double highestScore = 0.0;
                    
                    for (Map<String, Object> pair : labelScorePairs) {
                        String label = (String) pair.get("label");
                        Double score = (Double) pair.get("score");
                        logger.info("Processing: label={}, score={}", label, score);
                        
                        if (score > highestScore) {
                            highestScore = score;
                            bestMatch = pair;
                        }
                    }
                    
                    if (bestMatch != null) {
                        String label = (String) bestMatch.get("label");
                        logger.info("Best match - Label: {}, Score: {}", label, highestScore);
                        
                        // Extract star rating from label
                        if (label != null && label.contains("star")) {
                            String starPart = label.split(" ")[0];
                            try {
                                int stars = Integer.parseInt(starPart);
                                logger.info("Extracted star rating: {}", stars);
                                
                                // Convert star rating to sentiment:
                                // 1-2 stars = NEGATIVE
                                // 3 stars = NEUTRAL  
                                // 4-5 stars = POSITIVE
                                if (stars >= 4) {
                                    logger.info("Returning POSITIVE for {} stars", stars);
                                    return Sentiment.POSITIVE;
                                } else if (stars <= 2) {
                                    logger.info("Returning NEGATIVE for {} stars", stars);
                                    return Sentiment.NEGATIVE;
                                } else {
                                    logger.info("Returning NEUTRAL for {} stars", stars);
                                    return Sentiment.NEUTRAL;
                                }
                            } catch (NumberFormatException e) {
                                logger.error("Could not parse star rating from label: {}", label);
                            }
                        }
                    }
                }
            }
            
            logger.warn("Could not parse sentiment from response, defaulting to NEUTRAL");
            return Sentiment.NEUTRAL; // Default neutral
            
        } catch (Exception e) {
            logger.error("Error analyzing sentiment for text: {}", text, e);
            return Sentiment.NEUTRAL; // Default fallback
        }
    }
} 