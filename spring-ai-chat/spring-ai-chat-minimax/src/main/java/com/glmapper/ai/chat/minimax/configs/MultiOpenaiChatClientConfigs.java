package com.glmapper.ai.chat.minimax.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Multiple OpenAI-Compatible API Endpoints
 * 多个 OpenAI 兼容的 API 端点配置，区别于不同模型类型的客户端配置。
 *
 * @Classname MultiOpenaiChatClientConfigs
 * @Date 2025/5/28 14:27
 * @Created by glmapper
 */
@Configuration
public class MultiOpenaiChatClientConfigs {

    /**
     * 仅提供 chat_multi 使用的代码侧配置，避免将 OpenAiApi 这类基础 Bean 暴露给容器，
     * 从而影响 spring.ai.openai.* 的自动配置链路。
     *
     * @return MultiOpenaiCodeProperties
     */
    @Bean("multiOpenaiCodeProperties")
    public MultiOpenaiCodeProperties multiOpenaiCodeProperties() {
        return new MultiOpenaiCodeProperties(
                System.getenv("MINIMAX_API_KEY"),
                "https://api.minimaxi.com",
                "/v1/chat/completions",
                "MiniMax-M2.7-highspeed",
                System.getenv("ARK_API_KEY"),
                "https://ark.cn-beijing.volces.com",
                "/api/v3/chat/completions",
                "doubao-seed-2-0-mini-260215"
        );
    }

    public record MultiOpenaiCodeProperties(
            String minimaxApiKey,
            String minimaxBaseUrl,
            String minimaxCompletionsPath,
            String minimaxModel,
            String doubaoApiKey,
            String doubaoBaseUrl,
            String doubaoCompletionsPath,
            String doubaoModel
    ) {
    }
}
