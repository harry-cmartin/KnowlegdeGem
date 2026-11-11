package aichat.springaigemini.service;


import aichat.springaigemini.model.brief.BriefResponseRecord;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BriefService {

    private final ChatClient chatClient;

    public BriefService(ChatClient chatClient) {
        this.chatClient = chatClient;

    }

    public BriefResponseRecord createProductBriefRecord(String input) {

        return chatClient.prompt()
                .user(user -> user.text(
                        """
                              Transforme essa ideia de software ainda em desenvolvimento em um produto focado.
                              E apresente funcionalidades práticas, riscos e primeiros marcos.
                              
                              Input: {input}  
                                
                        """
                ).param("input", input) )
                .call()
                .entity(BriefResponseRecord.class, options -> options.
                        useProviderStructuredOutput()
                        .validateSchema()
                );
    }

    public Flux<String> createProductBriefRecordStreaming(String input) {
        return chatClient.prompt()
                .user(user -> user.text(
                        """
                              Transforme essa ideia de software ainda em desenvolvimento em um produto focado.
                              E apresente funcionalidades práticas, riscos e primeiros marcos.
                              
                              Input: {input}  
                                
                        """
                ).param("input", input) )
                .stream()
                .content();

    }

    public String reviewPhoto(byte[] photo, MediaType mediaType) {
        return chatClient.prompt()
                .user(user -> user.text(
                        """
                            Faça uma review dessa foto, focando em ser claro
                        """
                ).media(mediaType, new ByteArrayResource(photo)))
                .call()
                .content();

    }
}
