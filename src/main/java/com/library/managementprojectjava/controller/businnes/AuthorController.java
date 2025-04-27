package com.library.managementprojectjava.controller.businnes;

import com.library.managementprojectjava.payload.request.businnes.AuthorRequest;
import com.library.managementprojectjava.payload.response.businnes.AuthorResponse;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.service.businnes.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseMessage<AuthorResponse> saveAuthor(@RequestBody @Valid AuthorRequest authorRequest) {
        return authorService.saveAuthor(authorRequest);

    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> pageableAuthor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String type) {

        Page<AuthorResponse> response = authorService.getAllAuthorPageable(page, size, sort, type);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{authorId}")
    public ResponseMessage<AuthorResponse> getByIdAuthor(@PathVariable Long authorId) {
        return authorService.getAuthorByIdRes(authorId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{authorId}")
    public ResponseMessage<AuthorResponse> updateAuthor(@RequestBody @Valid AuthorRequest authorRequest,
                                                        @PathVariable Long authorId) {
        return authorService.updateAuthor(authorRequest, authorId);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{authorId}")
    public ResponseMessage<AuthorResponse> deleteAuthor(@PathVariable Long authorId){
        return authorService.deleteAuthor(authorId);
    }
}
