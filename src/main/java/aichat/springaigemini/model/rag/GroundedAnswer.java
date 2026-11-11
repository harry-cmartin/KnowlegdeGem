package aichat.springaigemini.model.rag;

import java.util.List;

public record GroundedAnswer(

        String answer,
        List<SourceReference> sources

) {
}
