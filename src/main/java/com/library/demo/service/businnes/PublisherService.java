package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Publisher;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.PublisherMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.PublisherRequest;
import com.library.demo.payload.response.businnes.PublisherResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.PublisherRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMappers publisherMappers;

    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PUBLISHER_NOT_FOUND));
    }

    public ResponseMessage<PublisherResponse> savePublisher(@Valid PublisherRequest publisherRequest) {
        Publisher publisher = publisherRepository.save(publisherMappers.mapPublisherRequestToPublisher(publisherRequest));
        return ResponseMessage.<PublisherResponse>builder()
                .message(SuccessMessages.PUBLISHER_CREATE)
                .returnBody(publisherMappers.mapPublisherToPublisherResponse(publisher))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public Page<PublisherResponse> getAllPublishers(int page, int size, String sort, String type) {
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<Publisher> publishers = publisherRepository.findAll(pageable);
        return publishers.map(publisherMappers::mapPublisherToPublisherResponse);
    }

    public ResponseMessage<PublisherResponse> getPublisherByIdRes(Long publisherId) {

        return ResponseMessage.<PublisherResponse>builder()
                .message(SuccessMessages.PUBLISHER_FOUND)
                .returnBody(publisherMappers.mapPublisherToPublisherResponse(getPublisherById(publisherId)))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}

