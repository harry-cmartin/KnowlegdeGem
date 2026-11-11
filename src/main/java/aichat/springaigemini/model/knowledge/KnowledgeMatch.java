package aichat.springaigemini.model.knowledge;

public record KnowledgeMatch(


        String documentId,
        String source,
        String text,
        Double score

) {
}
