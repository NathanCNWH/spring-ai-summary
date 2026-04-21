package com.glmapper.ai.chat.minimax.controller.request;

/**
 * 流式聊天请求
 * @author glmapper
 * @date 2026/04/21
 */
public class StreamChatRequest {

    /**
     * 用户输入
     */
    private String userInput;

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}