package aichat.springaigemini.model.rag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RagQuestion(

        @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{1,80}") String projectId,
        @NotBlank @Size(max = 100) String question

) {
}
