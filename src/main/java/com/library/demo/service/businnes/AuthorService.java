package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Author;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.repository.businnes.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author getAuthorById(Long id){
       return authorRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.AUTHOR_NOT_FOUND));

    }


}
