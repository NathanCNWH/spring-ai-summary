package com.glmapper.ai.chat.minimax.controller;

import com.glmapper.ai.chat.minimax.advisor.SimpleAdvisor;
import com.glmapper.ai.chat.minimax.controller.request.StreamChatRequest;
import com.glmapper.ai.chat.minimax.service.MultiClientService;
import com.glmapper.ai.chat.minimax.service.TemplateService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

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

    /**
     * 流式聊天接口
     *
     * @param request 流式聊天请求
     * @return 流式返回内容
     */
    @GetMapping(value = "/chat_stream", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> chatStream(StreamChatRequest request) {
        return chatClient.prompt()
                .user(request.getUserInput())
                .stream()
                .content();
    }
    /**
     * 流式聊天接口
     *
     * @param request 流式聊天请求
     * @return 流式返回内容
     */
    @GetMapping(value = "/chat_stream2", produces = "text/event-stream;charset=UTF-8")
    public ResponseEntity<Flux<String>> chatStream2(StreamChatRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.TEXT_EVENT_STREAM, StandardCharsets.UTF_8));
        headers.setCacheControl("no-cache");
        headers.set("Connection", "keep-alive");
        return ResponseEntity.ok()
                .headers(headers)
                .body(chatClient.prompt()
                        .user(request.getUserInput())
                        .stream()
                        .content());
    }
}
