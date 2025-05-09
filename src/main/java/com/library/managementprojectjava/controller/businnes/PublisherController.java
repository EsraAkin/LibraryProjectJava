package com.library.managementprojectjava.controller.businnes;

import com.library.managementprojectjava.payload.request.businnes.PublisherRequest;
import com.library.managementprojectjava.payload.response.businnes.PublisherResponse;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.service.businnes.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<PublisherResponse>> getAllPublishers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String type
    ) {
        Page<PublisherResponse> response = publisherService.getAllPublishers(page, size, sort, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publisherId}")
    public ResponseMessage<PublisherResponse> getPublisherById(@PathVariable Long publisherId) {
        return publisherService.getPublisherByIdRes(publisherId);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{publisherId}")
    public ResponseMessage<PublisherResponse> updatePublisher(@RequestBody @Valid PublisherRequest publisherRequest,
                                                              @PathVariable Long publisherId) {
        return publisherService.updatePublisher(publisherRequest, publisherId);

    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{publisherId}")
    public ResponseMessage<PublisherResponse> deletePublisher(@PathVariable Long publisherId) {
        return publisherService.deletePublisher(publisherId);

    }


}
