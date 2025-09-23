package com.suraj.ecom.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class ChatMemoryController {
	
	public record MessageResponse(UUID conversationId, String message) {};
	
    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

    private final ChatMemoryRepository chatMemoryRepository;


    public ChatMemoryController(ChatClient.Builder chatClientBuilder, ChatMemoryRepository chatMemoryRepository) {
        this.chatMemoryRepository = chatMemoryRepository;
        this.chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(chatMemoryRepository)
                .build();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor
                        .builder(chatMemory)
                        .build())
                .build();
    }

    @PostMapping("/memory/chat")
    public MessageResponse chat(@RequestParam(required = false) UUID conversationId, @RequestBody String message) {
        UUID currentConvId = conversationId == null ?  UUID.randomUUID() : conversationId;
        MessageResponse messageResponse;
        if (conversationId == null) {
            messageResponse =  new MessageResponse(currentConvId, chatClient.prompt()
                    .user(message)
                    .call().content());
        } else {
            messageResponse =  new MessageResponse(currentConvId, chatClient.prompt()
                    .user(message)
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, currentConvId))
                    .call().content());
        }
        return messageResponse;
    }
    
    @GetMapping("/history")
    public List<String> findAllConversation() {
        return chatMemoryRepository.findConversationIds();
    }

   
}
