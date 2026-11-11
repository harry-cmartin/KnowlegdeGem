package aichat.springaigemini.controller;


import aichat.springaigemini.model.conversation.ConversationRequest;
import aichat.springaigemini.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/conversation")
public class ConversationController {


    private ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public Map< String , String> chat (@Valid @RequestBody ConversationRequest conversationRequest) {

        return Map.of(
                "answer",
                conversationService.chat(
                        conversationRequest.conversationId(),
                        conversationRequest.message()


                )

        );

    }

    @DeleteMapping("/{conversationId}")
    ResponseEntity<Void> clear(@PathVariable String conversationId) {

        conversationService.clear(conversationId);
        return ResponseEntity.noContent().build();

    }


}
