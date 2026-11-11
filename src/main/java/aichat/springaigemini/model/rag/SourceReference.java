package aichat.springaigemini.model.rag;

public record SourceReference(

        String documentId,
        String source,
        Double score
) {
}
