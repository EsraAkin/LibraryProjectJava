package com.library.managementprojectjava.service.businnes;

import com.library.managementprojectjava.entity.businnes.Publisher;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.exception.ResourceNotFoundException;
import com.library.managementprojectjava.payload.mappers.PublisherMappers;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.messages.SuccessMessages;
import com.library.managementprojectjava.payload.request.businnes.PublisherRequest;
import com.library.managementprojectjava.payload.response.businnes.PublisherResponse;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.repository.businnes.PublisherRepository;
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

    public ResponseMessage<PublisherResponse> updatePublisher(@Valid PublisherRequest publisherRequest, Long publisherId) {

        // 1. Get current publisher
        Publisher existingPublisher = getPublisherById(publisherId);

        // 2. If it is a built-in publisher, it cannot be updated.
        if (Boolean.TRUE.equals(existingPublisher.getBuiltIn())) {
            throw new ConflictException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // 3. Updating
        existingPublisher.setName(publisherRequest.getName());

        existingPublisher.setId(publisherId);

        // 4. Save To DB
        Publisher updatedPublisher = publisherRepository.save(existingPublisher);

        // 5. Convert to DTO and return
        return ResponseMessage.<PublisherResponse>builder()
                .message(SuccessMessages.PUBLISHER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .returnBody(publisherMappers.mapPublisherToPublisherResponse(updatedPublisher))
                .build();
    }

    public ResponseMessage<PublisherResponse> deletePublisher(Long publisherId) {
        // 1. Get current publisher
        Publisher existingPublisher = getPublisherById(publisherId);

        //2. delete map and return
        publisherRepository.deleteById(publisherId);
        return ResponseMessage.<PublisherResponse>builder()
                .message(SuccessMessages.PUBLISHER_DELETE)
                .returnBody(publisherMappers.mapPublisherToPublisherResponse(existingPublisher))
                .httpStatus(HttpStatus.OK)
                .build();

    }
}

