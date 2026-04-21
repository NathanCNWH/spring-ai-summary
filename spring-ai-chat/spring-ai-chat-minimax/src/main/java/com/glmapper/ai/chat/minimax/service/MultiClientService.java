package com.glmapper.ai.chat.minimax.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
