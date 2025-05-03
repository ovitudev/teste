package br.com.chatbot.furia_tech.service;

import br.com.chatbot.furia_tech.dto.ChatGeminiRequest;
import br.com.chatbot.furia_tech.dto.ChatGeminiResponse;
import br.com.chatbot.furia_tech.dto.PromptRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatGeminiService {

    // URL referente a API aonde a IA irá coletar as informações sobre a FURIA
    private static final String API_URL = "https://api.pandascore.co/csgo/matches";

    @Value(value = "${pandascore.api-token}")
    private String pandaApiToken;

    @Value(value = "${spring.ai.vertex.ai.gemini.api-key}")
    private String geminiApiKey;

    // Configurada na classe GeminiAPIConfiguration
    private final RestClient restClient;

    public ChatGeminiService(RestClient restClient) {
        this.restClient = restClient;
    }

    // Metodo responsável por retornar a resposta da IA, referente a pergunta recebida pelo prompt
    public String getChatResponse(PromptRequest promptRequest){
        String furiaData = fetchFuriaMatches();

        ChatGeminiRequest geminiRequest = getChatGeminiRequest(promptRequest, furiaData);

        ChatGeminiResponse response = restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey)
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .body(geminiRequest)
                .retrieve()
                .body(ChatGeminiResponse.class);

        return response.candidates().getFirst().content().parts().getFirst().text();
    }

    private static ChatGeminiRequest getChatGeminiRequest(PromptRequest promptRequest, String furiaData) {
        // Prompt que será passado para a IA contendo a pergunta do usuário e restrições para a IA
        String fullPrompt = String.format(
                """
                 A seguir estão dados de partidas futuras da Furia no CS:GO: %s
                 Responda à pergunta: %s
                 (Caso a pergunta não seja sobre o time Furia/FURIA/furia no CS, informe que não consegue responder
                 e em casos de ser uma pergunta relacionada a Furia, mas nao sobre a partidas futuras responda o que
                 souber!)"""
                ,
                furiaData,
                promptRequest.prompt()
        );

        ChatGeminiRequest geminiRequest = new ChatGeminiRequest(
                List.of(new ChatGeminiRequest.Content("user", List.of(new ChatGeminiRequest.Part(fullPrompt)))),
                new ChatGeminiRequest.GenerationConfig(2048)
        );
        return geminiRequest;
    }

    // Metodo responsável por coletar informações relevantes para o chat
    // Como os próximos times oponentes da FURIA, data de jogos, dentre outras informações
    private String fetchFuriaMatches() {

        RestTemplate restTemplate = new RestTemplate();          // Comunicação Http com API
        HttpHeaders  headers      = new HttpHeaders();           // Cabeçalho de solicitação Http

        headers.set("Authorization", "Bearer " + pandaApiToken); // Informa a Key API para o header
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Realiza uma Requisição Http enviando uma requisição REST
        ResponseEntity<String> response = restTemplate.exchange(
                API_URL,        // API URL da pandascore
                HttpMethod.GET, // Metodo utilizado
                entity,         // Headers utilizados
                String.class    // Resposta esperada no tipo String
        );

        // Conversão do corpo da resposta HTTP para um Array JSON
        JSONArray matches = new JSONArray(response.getBody());
        StringBuilder summary = new StringBuilder();

        // Será filtrada informações dos oponentes e datas, apenas ligadas a FURIA
        for (int i = 0; i < matches.length(); i++) {
            JSONObject match = matches.getJSONObject(i);

            // Obtém os dois oponentes da partida ( caso existam )
            JSONObject opponent1 = match.getJSONArray("opponents").optJSONObject(0);
            JSONObject opponent2 = match.getJSONArray("opponents").optJSONObject(1);

            // Verifica se a FURIA está envolvida
            if (opponent1 != null && opponent2 != null &&
                    (opponent1.getJSONObject("opponent").getString("name").equalsIgnoreCase("FURIA") ||
                            opponent2.getJSONObject("opponent").getString("name").equalsIgnoreCase("FURIA"))) {

                // Extrai os nomes dos times, status da partida e data de início
                String team1 = opponent1.getJSONObject("opponent").getString("name");
                String team2 = opponent2.getJSONObject("opponent").getString("name");
                String status = match.getString("status");
                String date = match.getString("begin_at");

                // Adiciona as informações formatadas ao resumo
                summary.append(String.format("Partida: %s vs %s | Status: %s | Data: %s\n", team1, team2, status, date));
            }
        }

        // Retorna o resultado obtido através dos filtros aplicados e informações solicitadas
        return summary.toString();
    }
}
