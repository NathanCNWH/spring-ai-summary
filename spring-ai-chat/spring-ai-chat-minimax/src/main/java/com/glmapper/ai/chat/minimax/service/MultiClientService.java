package com.glmapper.ai.chat.minimax.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OpenAiApi baseOpenAiApi;

    public String multiClientFlow() {
        //这里可以覆盖初始配置
        OpenAiApi deepseekApi = baseOpenAiApi.mutate()
                .baseUrl("https://api.minimaxi.com")
                .completionsPath("/v1/chat/completions")
                .build();

        OpenAiChatModel deepseekModel = baseChatModel.mutate()
                .openAiApi(deepseekApi)
                .defaultOptions(OpenAiChatOptions.builder().model("MiniMax-M2.7-highspeed").temperature(0.5).build())
                .build();

        return ChatClient.builder(deepseekModel).build().prompt("你好").call().content();
    }

    public String imageClientFlowMedia(String imageUrl, String question) throws MalformedURLException {
        // 豆包 Chat Completions API 配置
        OpenAiApi doubaoApi = baseOpenAiApi.mutate()
                .baseUrl("https://ark.cn-beijing.volces.com")
                .apiKey(System.getenv("ARK_API_KEY"))
                .completionsPath("/api/v3/chat/completions")
                .build();

        OpenAiChatModel doubaoModel = baseChatModel.mutate()
                .openAiApi(doubaoApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("doubao-seed-2-0-mini-260215")
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