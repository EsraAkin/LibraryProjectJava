package com.library.managementprojectjava.controller.businnes;

import com.library.managementprojectjava.payload.request.businnes.BookRequest;
import com.library.managementprojectjava.payload.response.businnes.BookResponse;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.service.businnes.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;


    @GetMapping("/test/admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String testAdmin() {
        return "Hello Admin!";
    }


    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseMessage<BookResponse> saveBook(@RequestBody @Valid BookRequest bookRequest) {
        return bookService.saveBook(bookRequest);
    }


    @GetMapping
    public Page<BookResponse> getBooks(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long cat,
            @RequestParam(required = false) Long author,
            @RequestParam(required = false) Long publisher,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String type
    ) {
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return bookService.getBooks(q, cat, author, publisher, pageable);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MEMBER')")
    @GetMapping("/{bookId}")
    public ResponseMessage<BookResponse> getBookId(@PathVariable Long bookId){
        return bookService.getBookById(bookId);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<ResponseMessage<BookResponse>> updateBook(@PathVariable Long bookId,
                                                                    @RequestBody @Valid BookRequest bookRequest){
        ResponseMessage<BookResponse> response = bookService.updateBook(bookId, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{book_Id}")
    public ResponseMessage<BookResponse> deleteBook(@PathVariable Long book_Id) {
       return bookService.deleteBookById(book_Id);

    }

}
