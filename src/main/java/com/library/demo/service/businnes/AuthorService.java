package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Author;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.AuthorMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.AuthorRequest;
import com.library.demo.payload.response.businnes.AuthorResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.AuthorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
