package com.gaoyx.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 *
 */
@RestController
@RequestMapping("/ai")
public class DeepSeekChatController {

    @Resource
    private DeepSeekChatModel deepSeekChatModel;

    /**
     * 普通对话
     * @param message 消息
     * @return ai 回答
     */
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "你好") String message) {
        // 一次性返回结果
        return deepSeekChatModel.call(message);
    }

    /**
     * 流式输出
     * @param message 消息
     * @return 每个响应片段的文本内容
     */
    @GetMapping(value = "/generateStream", produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "你好") String message) {
        // 构建提示词
        Prompt prompt = new Prompt(new UserMessage(message));

        // 流式输出
        return deepSeekChatModel.stream(prompt)
                .mapNotNull(chatResponse ->
                        chatResponse.getResult()
                                    .getOutput()
                                    .getText());
    }
}
