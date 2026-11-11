package aichat.springaigemini.service;


import jakarta.validation.constraints.Size;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ConversationService(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    public String chat(String conversationId, String message) {

        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId

                ))
                .call()
                .content();
    }

    public void clear(String conversationId) {

        chatMemory.clear(conversationId);

    }
}
