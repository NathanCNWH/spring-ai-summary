package com.glmapper.ai.chat.minimax.service;

import com.glmapper.ai.chat.minimax.configs.MultiOpenaiChatClientConfigs.MultiOpenaiCodeProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

@Component
public class MultiClientService {

    @Autowired
    private OpenAiChatModel baseChatModel;

    @Autowired
    @Qualifier("multiOpenaiCodeProperties")
    private MultiOpenaiCodeProperties multiOpenaiCodeProperties;

    public String multiClientFlow() {
        OpenAiApi minimaxApi = OpenAiApi.builder()
                .apiKey(multiOpenaiCodeProperties.minimaxApiKey())
                .baseUrl(multiOpenaiCodeProperties.minimaxBaseUrl())
                .completionsPath(multiOpenaiCodeProperties.minimaxCompletionsPath())
                .build();

        OpenAiChatModel minimaxModel = baseChatModel.mutate()
                .openAiApi(minimaxApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(multiOpenaiCodeProperties.minimaxModel())
                        .temperature(0.5)
                        .build())
                .build();

        return ChatClient.builder(minimaxModel).build().prompt("你好").call().content();
    }

    public String imageClientFlowMedia(String imageUrl, String question) throws MalformedURLException {
        OpenAiApi doubaoApi = OpenAiApi.builder()
                .apiKey(multiOpenaiCodeProperties.doubaoApiKey())
                .baseUrl(multiOpenaiCodeProperties.doubaoBaseUrl())
                .completionsPath(multiOpenaiCodeProperties.doubaoCompletionsPath())
                .build();

        OpenAiChatModel doubaoModel = baseChatModel.mutate()
                .openAiApi(doubaoApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(multiOpenaiCodeProperties.doubaoModel())
                        .build())
                .build();

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(List.of(
                        Media.builder()
                                .mimeType(MediaType.IMAGE_PNG)
                                .data(URI.create(imageUrl))
                                .build()))
                .build();

        return ChatClient.builder(doubaoModel).build()
                .prompt()
                .messages(userMessage)
                .call()
                .content();
    }

}
