package aichat.springaigemini.service;


import aichat.springaigemini.model.knowledge.KnowledgeDocumentRequest;
import aichat.springaigemini.model.knowledge.KnowledgeMatch;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgeService {

    private final VectorStore vectorStore;

    private final TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
            .withChunkSize(512)
            .withMinChunkSizeChars(200)
            .withMinChunkLengthToEmbed(20)
            .withMaxNumChunks(100)
            .withKeepSeparator(true)
            .build();

    public KnowledgeService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /*
    public int index(KnowledgeDocumentRequest request) {

            var document = new Document(
                request.content(),
                Map.of(
                    "projectId", request.projectId(),
                    "source", request.source()

                )
            );

        vectorStore.add(List.of(document));
        return 1;
    }


      */


    public int index(KnowledgeDocumentRequest request) {

        var sourceDocument = new Document(
                request.content(),
                Map.of(
                        "projectId", request.projectId(),
                        "source", request.source()

                )
        );

        var chunck = tokenTextSplitter.apply(List.of(sourceDocument));
        vectorStore.add(chunck);

        return chunck.size();
    }

    public List<KnowledgeMatch> search(String projectId, String query) {

        /*
         * Na indexação, o KnowledgeDocumentRequest é transformado em um
         * org.springframework.ai.document.Document: o content vira o texto,
         * projectId e source são salvos nos metadados, e o vector store
         * gera o embedding e armazena o documento.
         *
         * Na busca, similaritySearch recupera esses Documents do vector store.
         * Por isso, cada campo do KnowledgeMatch vem do Document retornado:
         * - documentId: ID do Document, gerado/armazenado pelo vector store;
         * - source: metadado "source", inserido durante a indexação;
         * - text: conteúdo original, recuperado por getText();
         * - score: similaridade calculada entre a consulta e o documento.
         */
        return  searchDocuments(projectId, query)
                .stream()
                .map(document -> new KnowledgeMatch(
                        document.getId(),
                        String.valueOf(document.getMetadata().get("source")),
                        document.getText(),
                        document.getScore()

                        
                )).toList();

    }

    public List<Document> searchDocuments(String projectId, String query){
            return vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(query)
                            .topK(5)
                            .similarityThreshold(0.60)
                            .filterExpression("projectId == '" + projectId + "'")
                            .build()
            );

    }

}
