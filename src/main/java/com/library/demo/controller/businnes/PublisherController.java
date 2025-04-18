package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.PublisherRequest;
import com.library.demo.payload.response.businnes.PublisherResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseMessage<PublisherResponse> savePublisher(@RequestBody @Valid PublisherRequest publisherRequest) {
        return publisherService.savePublisher(publisherRequest);
    }


}
