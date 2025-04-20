package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.AuthorRequest;
import com.library.demo.payload.response.businnes.AuthorResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/authors")
    public ResponseMessage<AuthorResponse> saveAuthor(@RequestBody @Valid AuthorRequest authorRequest){
        return authorService.saveAuthor(authorRequest);

    }

}
