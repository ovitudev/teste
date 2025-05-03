package br.com.chatbot.furia_tech.dto;

import java.util.List;

// Classe responsável pelas requisições, obtendo o conteúdo,
// e o valor máximo de cada Requisição enviada. Sendo valor
// de Input e Output (entrada e saída total).
public record ChatGeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig
) {
    public record Content(String role, List<Part> parts){}
    public record Part(String text){}
    public record GenerationConfig(int maxOutputTokens){}
}
