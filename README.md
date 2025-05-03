 # 🤖 FURIA CS:GO Fan Chatbot

Este projeto tem como objetivo criar uma experiência conversacional voltada para fãs do time de CS:GO da FURIA. Desenvolvido em Java com Spring Boot, o chatbot utiliza a API **Gemini** da Google para gerar respostas baseadas em dados reais dos jogos, fornecidos pela **API PandaScore**.

## 🚀 Funcionalidades

- Integração com a API Gemini (`gemini-2.0-flash`) para geração de texto.
- Consumo da API PandaScore para recuperar dados atualizados de partidas da FURIA no CS:GO.
- Requisição e resposta via endpoint `/api/chat`, utilizando um prompt construído dinamicamente.
- Validação para garantir que o chatbot responda apenas sobre o time de **CS:GO da FURIA**.
- Uma landing page simples para fins de interface com o usuário.

## 📦 Tecnologias

- Java 21
- Spring Boot
- Spring Web
- RestTemplate / RestClient
- Gemini API (Google Generative Language)
- PandaScore API
- Thymeleaf (para renderização da landing page)
- JSON
- Maven

## 📄 Como executar

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/furia-cs-chatbot.git
cd furia-cs-chatbot
```

2. Configure as chaves de API no seu `application.properties`:

```properties
spring.ai.vertex.ai.gemini.api-key=SUA_GEMINI_API_KEY
pandascore.api-token=SEU_PANDASCORE_TOKEN
```

3. Compile e execute:

```bash
./mvnw spring-boot:run
```

4. Acesse:

- Landing page: `http://localhost:8080/`
- API de chat: `POST http://localhost:8080/api/chat` com o corpo:

```json
{
  "prompt": "Quando será o próximo jogo da FURIA?"
}
```

## ⚠️ Observações

- Atualmente o sistema realiza **uma única chamada** à API do PandaScore, por conta das **limitações de custo** da plataforma.
- Para uma experiência mais rica e precisa, seria interessante futuramente integrar com uma **API própria ou com cache** que traga **estatísticas mais relevantes** dos jogos, jogadores e eventos.
- A landing page foi **gerada com auxílio de uma IA**, já que o foco principal deste projeto está no **backend** e na lógica conversacional com IA.

## 💡 Próximos passos

- Cache ou banco local para armazenar informações dos jogos.
- Novos endpoints para buscar estatísticas, jogadores ou notícias.
- Integração com Telegram ou WhatsApp.
- Simulador de torcida com IA.

---
