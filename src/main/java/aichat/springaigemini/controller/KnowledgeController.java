package aichat.springaigemini.controller;


import aichat.springaigemini.model.knowledge.KnowledgeDocumentRequest;
import aichat.springaigemini.model.knowledge.KnowledgeMatch;
import aichat.springaigemini.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping
    public Map<String, Integer> createKnowledge(@Valid @RequestBody KnowledgeDocumentRequest request){
        return Map.of("chuncknized", knowledgeService.index(request));
    }


    @GetMapping("/search")
    public List<KnowledgeMatch> search(@Valid @RequestParam String projectId,
                                       @Valid @RequestParam String query){

        return knowledgeService.search(projectId, query);
    }
}
