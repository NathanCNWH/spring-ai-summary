package com.glmapper.ai.chat.minimax.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {


    @Autowired
    @Qualifier("defaultChatClient")
    private ChatClient chatClient;


    public String chatPrompt(String userInput, Integer type) {
        //固定对话
        if(type==1){
            // chatClient.prompt().user(userInput).call().chatClientResponse();
            // chatClient.prompt().user(userInput).call().chatResponse();
            return chatClient.prompt().user(userInput).call().content();

        } else if (type==2) {
            //占位符
            return chatClient.prompt().user(promptUserSpec ->
                            promptUserSpec.text("使用 json 格式输出这段文字： {content}")
                                    .param("content", userInput)
                    )
                    .call().content();



        } else if (type==3) {
            //自定义占位符

            return chatClient.prompt().user(promptUserSpec ->
                            promptUserSpec.text("使用 json 格式输出这段文字： <content>")
                                    .param("content", userInput)
                    )
                    .templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                    .call().content();
        }


        return "error";
    }
}
