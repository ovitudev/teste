package br.com.chatbot.furia_tech.dto;

import java.util.List;

// Classe responsável pelas respostas geradas pela IA
public record ChatGeminiResponse(
        List<Candidate> candidates
) {
    public record Candidate(Content content){}
    public record Content(String role, List<Part> parts){}
    public record Part(String text){}
}
