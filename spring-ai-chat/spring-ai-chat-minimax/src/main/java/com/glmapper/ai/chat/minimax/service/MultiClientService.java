package com.glmapper.ai.chat.minimax.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

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

    /**
     * 豆包视觉识别（使用 Responses API 格式）
     *
     * @param imageUrl 图片URL
     * @param question 问题/指令
     * @return AI回答
     */
    public String imageClientFlow(String imageUrl, String question) throws MalformedURLException {
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

        // 构建多模态消息：图片 + 文本
        JSONArray content = new JSONArray();
        content.add(JSONObject.of("type", "input_image")
                .fluentPut("image_url", java.net.URI.create(imageUrl).toURL().toString())
        );
        content.add(JSONObject.of("type", "input_text")
                .fluentPut("text", question)
        );
        UserMessage userMessage = new UserMessage(content.toJSONString());

        return ChatClient.builder(doubaoModel).build()
                .prompt()
                .messages(userMessage)
                .call()
                .content();
    }

}