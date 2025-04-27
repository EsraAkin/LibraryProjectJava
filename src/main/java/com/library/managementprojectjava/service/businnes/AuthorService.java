package com.library.managementprojectjava.service.businnes;

import com.library.managementprojectjava.entity.businnes.Author;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.exception.ResourceNotFoundException;
import com.library.managementprojectjava.payload.mappers.AuthorMappers;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.messages.SuccessMessages;
import com.library.managementprojectjava.payload.request.businnes.AuthorRequest;
import com.library.managementprojectjava.payload.response.businnes.AuthorResponse;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.repository.businnes.AuthorRepository;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMappers authorMappers;

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND));

    }


    public ResponseMessage<AuthorResponse> saveAuthor(@Valid AuthorRequest authorRequest) {
        Author author = authorRepository.save(authorMappers.mapAuthorRequestToAuthor(authorRequest));
        return ResponseMessage.<AuthorResponse>builder()
                .message(SuccessMessages.AUTHOR_CREATE)
                .returnBody(authorMappers.mapAuthorToAuthorResponse(author))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public Page<AuthorResponse> getAllAuthorPageable(int page, int size, String sort, String type) {
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(authorMappers::mapAuthorToAuthorResponse);


    }

    public ResponseMessage<AuthorResponse> getAuthorByIdRes(Long authorId) {
        return ResponseMessage.<AuthorResponse>builder()
                .message(SuccessMessages.AUTHOR_FOUND)
                .returnBody(authorMappers.mapAuthorToAuthorResponse(getAuthorById(authorId)))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<AuthorResponse> updateAuthor(@Valid AuthorRequest authorRequest, Long authorId) {
        Author existingAuthor = getAuthorById(authorId);
        if (Boolean.TRUE.equals(existingAuthor.getBuiltIn())) {
            throw new ConflictException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
        existingAuthor.setName(authorRequest.getName());
        Author updatedAuthor = authorRepository.save(existingAuthor);
        return ResponseMessage.<AuthorResponse>builder()
                .message(SuccessMessages.AUTHOR_UPDATE)
                .httpStatus(HttpStatus.OK)
                .returnBody(authorMappers.mapAuthorToAuthorResponse(updatedAuthor))
                .build();
    }


    public ResponseMessage<AuthorResponse> deleteAuthor(Long authorId) {

        Author existingAuthor = getAuthorById(authorId);
        authorRepository.deleteById(authorId);

        return ResponseMessage.<AuthorResponse>builder()
                .message(SuccessMessages.AUTHOR_DELETE)
                .returnBody(authorMappers.mapAuthorToAuthorResponse(existingAuthor))
                .httpStatus(HttpStatus.OK)
                .build();

    }
}

