package com.glmapper.ai.chat.minimax.controller;

import com.glmapper.ai.chat.minimax.advisor.SimpleAdvisor;
import com.glmapper.ai.chat.minimax.service.MultiClientService;
import com.glmapper.ai.chat.minimax.service.TemplateService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ChatController
 * @Description qwen ChatController
 * @Date 2025/5/28 13:51
 * @Created by glmapper
 */
@RestController
@RequestMapping("/api/minimax")
public class ChatController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private SimpleAdvisor simpleAdvisor;
    @Autowired
    private MultiClientService multiClientService;

    /**
     * 普通的聊天接口
     *
     * @param userInput 用户输入
     * @return 返回内容
     */
    @GetMapping("/chat")
    public String prompt(@RequestParam String userInput) {
        return this.chatClient.prompt().user(userInput).call().content();
    }
    /**
     * 模板的聊天接口
     *
     * @param userInput 用户输入
     * @return 返回内容
     */
    @GetMapping("/chatPrompt")
    public String chatPrompt(@RequestParam String userInput,@RequestParam Integer type) {
        return templateService.chatPrompt(userInput,type);
    }
    /**
     * advisor拦截器
     *
     * @param userInput 用户输入
     * @return 返回内容
     */
    @GetMapping("/chat_advisor")
    public String chat_advisor(@RequestParam String userInput) {
        return chatClient.prompt().advisors(simpleAdvisor).user(userInput).call().content();
    }
    /**
     * 多配置
     *
     * @return 返回内容
     */
    @GetMapping("/chat_multi")
    public String chat_multi() {
        return multiClientService.multiClientFlow();
    }
}
