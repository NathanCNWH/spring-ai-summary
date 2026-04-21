package com.glmapper.ai.chat.minimax.advisor;

import io.micrometer.core.instrument.Metrics;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.stereotype.Component;

@Component
public class SimpleAdvisor implements CallAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        Usage usage = chatClientResponse.chatResponse().getMetadata().getUsage();
        Integer promptTokens = usage.getPromptTokens();
        Metrics.counter("ai.prompt.tokens").increment(promptTokens);

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "SimpleAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
