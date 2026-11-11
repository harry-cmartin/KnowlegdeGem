package aichat.springaigemini.model.conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ConversationRequest(
        @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{1,80}") String conversationId,
        @NotBlank @Size(max = 10_000) String message


) {
}
