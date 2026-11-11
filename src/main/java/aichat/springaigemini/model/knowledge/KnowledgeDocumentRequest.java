package aichat.springaigemini.model.knowledge;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KnowledgeDocumentRequest(


        @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{1,80}") String projectId,
        @NotBlank @Size(max = 200) String source,
        @NotBlank @Size(max = 20_000) String content)

{


}
