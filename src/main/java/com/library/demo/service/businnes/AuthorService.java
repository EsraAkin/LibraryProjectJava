package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Author;
import com.library.demo.entity.businnes.Publisher;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.AuthorMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.AuthorRequest;
import com.library.demo.payload.response.businnes.AuthorResponse;
import com.library.demo.payload.response.businnes.PublisherResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.AuthorRepository;
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

    public Author getAuthorById(Long id){
       return authorRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND));

    }


    public ResponseMessage<AuthorResponse> saveAuthor(@Valid AuthorRequest authorRequest) {
       Author author= authorRepository.save(authorMappers.mapAuthorRequestToAuthor(authorRequest));
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
}
