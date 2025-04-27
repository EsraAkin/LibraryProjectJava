package com.library.managementprojectjava.payload.mappers;

import com.library.managementprojectjava.entity.businnes.Author;
import com.library.managementprojectjava.payload.request.businnes.AuthorRequest;
import com.library.managementprojectjava.payload.response.businnes.AuthorResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Builder
public class AuthorMappers {

    public Author mapAuthorRequestToAuthor(AuthorRequest authorRequest){
        return Author.builder()
                .name(authorRequest.getName())
                .builtIn(authorRequest.getBuiltIn())
                .build();

    }

    public AuthorResponse mapAuthorToAuthorResponse(Author author){
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .builtIn(author.getBuiltIn())
                .build();

    }


}
