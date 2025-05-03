package br.com.chatbot.furia_tech.controller;

import br.com.chatbot.furia_tech.dto.PromptRequest;
import br.com.chatbot.furia_tech.service.ChatGeminiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/api/chat")
public class ChatGeminiController {

    @GetMapping
    public ModelAndView screenChat(){
        return new ModelAndView("index");
    }

    private final ChatGeminiService chatGeminiService;

    public ChatGeminiController(ChatGeminiService chatGeminiService) {
        this.chatGeminiService = chatGeminiService;
    }

    @PostMapping
    // Metodo responsável pela requisição POST no sistema,
    // onde é necessário um corpo contendo uma Requisição de prompt,
    // ou melhor dizendo, uma entrada de informação do usuário
    public String chat(@RequestBody PromptRequest promptRequest){
        return chatGeminiService.getChatResponse(promptRequest); // Retorna a resposta da IA
    }
}
