package aichat.springaigemini.controller;


import aichat.springaigemini.model.rag.GroundedAnswer;
import aichat.springaigemini.model.rag.RagQuestion;
import aichat.springaigemini.service.RagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    public final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;

    }

    @PostMapping("/answer")
    public GroundedAnswer rag(@Valid @RequestBody RagQuestion ragQuestion){
        return ragService.answer(ragQuestion.projectId(), ragQuestion.question());

    }

    @PostMapping("/answerAdvisor")
    public GroundedAnswer advisor(@Valid @RequestBody RagQuestion ragQuestion){
        return ragService.answerWithAdvisor(ragQuestion.projectId(), ragQuestion.question());

    }

}
