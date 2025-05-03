package br.com.chatbot.furia_tech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GeminiAPIConfiguration {

    // Valor referente a API do Gemini
    @Value("${https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash}")
    private String apiURL;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiURL)
                .build();
    }
}
