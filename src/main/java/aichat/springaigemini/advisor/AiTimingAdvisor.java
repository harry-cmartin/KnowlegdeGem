package aichat.springaigemini.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


@Component
public class AiTimingAdvisor implements CallAdvisor {
    private static final Logger log = LoggerFactory.getLogger(AiTimingAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {


        var startTime = System.nanoTime();

        try {
            return chain.nextCall(request) ;
        }finally {

            var elapsedTime = (System.nanoTime() - startTime) / 1_000_000;


            log.info("Request para a IA finalizou em {} ms" ,elapsedTime);
        }

        
    }

    @Override
    public String getName() {
        return AiTimingAdvisor.class.getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
