package aichat.springaigemini.controller;


import aichat.springaigemini.model.brief.ClientRequestRecord;
import aichat.springaigemini.model.brief.BriefResponseRecord;
import aichat.springaigemini.service.BriefService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/briefs")
public class BriefController {

    private final BriefService briefService;

    private final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    private final Set<String> IMAGE_TYPES_SUPPORTED = Set.of(
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    );

    public BriefController(BriefService briefService) {
        this.briefService = briefService;
    }

    @PostMapping
    BriefResponseRecord createBrief(@Valid @RequestBody ClientRequestRecord clientRequest) {

        return briefService.createProductBriefRecord(clientRequest.input());

    }

    @PostMapping(value = "/pitch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamBrief(@Valid @RequestBody ClientRequestRecord clientRequest) {

        return briefService.createProductBriefRecordStreaming(clientRequest.input());

    }

    @PostMapping(value = "/review-photo" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, String> reviewPhoto(@RequestParam("image") MultipartFile photo) throws IOException {

        if(photo.isEmpty() || photo.getContentType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Arquivo não disponibilizado"
            );
        }

        if(!IMAGE_TYPES_SUPPORTED.contains(photo.getContentType())) {

            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,

                    "A imagem deve ser dos seguintes tipos de arquivo:  " + IMAGE_TYPES_SUPPORTED
                    );
        }

        if (photo.getSize() > MAX_IMAGE_SIZE) {

            throw new ResponseStatusException(HttpStatus.valueOf(415),

                    "O tamanho deve ser de no maximo " + MAX_IMAGE_SIZE

                    );

        }

        var mediaType = MediaType.parseMediaType(photo.getContentType());

        return Map.of( "review" , briefService.reviewPhoto(photo.getBytes() , mediaType));

    }


}
