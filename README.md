# EventSync - Event Management with Sentiment Analysis

A RESTful API built with Spring Boot that manages events and analyzes user feedback sentiment using AI.

## Collaborators
- valdas.trakumas@ibm.com

## Features

- **Event Management**: Create and retrieve events with unique IDs, titles, and descriptions
- **Feedback System**: Submit feedback for events with automatic sentiment analysis
- **AI Sentiment Analysis**: Integrates with Hugging Face API for real-time sentiment classification
- **Sentiment Summary**: Get detailed sentiment breakdown for each event
- **H2 Database**: In-memory database for easy development and testing

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **H2 Database**
- **WebClient** (for external API calls)
- **Hugging Face API** (for sentiment analysis)

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd demo
   ```
2. **Update the Hugging Face API Token

This project uses the Hugging Face API for inference, and the API token is configured in `src/main/resources/application.properties`.  

To change the token:  

2.1. Go to [Hugging Face > Access Tokens](https://huggingface.co/settings/tokens) and create a new **Access Token** with *read* permissions.  
    **Note:** When a token is exposed publicly (e.g., pushed to GitHub), Hugging Face automatically revokes it for security reasons. 

2.2. Open `src/main/resources/application.properties` and update the value for `huggingface.api.token`:  

   ```properties
   # Hugging Face API Configuration
   huggingface.api.url=https://api-inference.huggingface.co/models/nlptown/bert-base-multilingual-uncased-sentiment
   huggingface.api.token=hf_your_new_token_here
```

3. **Run the application**
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## API Endpoints

### 1. Create Event
POST /api/events

### 2. Get All Events
GET /api/events

### 3. Get Event by ID
GET /api/events/{eventId}

### 4. Submit Feedback
POST /api/events/{eventId}/feedback

### 5. Get Sentiment Summary
GET /api/events/{eventId}/summary

## Sentiment Analysis

The application uses the Hugging Face API for sentiment analysis:

- **Model**: `nlptown/bert-base-multilingual-uncased-sentiment`
- **Sentiment Categories**: POSITIVE, NEUTRAL, NEGATIVE
