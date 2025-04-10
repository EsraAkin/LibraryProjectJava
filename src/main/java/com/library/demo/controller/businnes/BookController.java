package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.BookService;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'MEMBER')")
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


    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'MEMBER')")
    @GetMapping("/{bookId}")
    public ResponseMessage<BookResponse> getBookId(@PathVariable Long bookId){
        return bookService.getBookById(bookId);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<ResponseMessage<BookResponse>> updateBook(@PathVariable Long bookId,
                                                                    @RequestBody @Valid BookRequest bookRequest){
        ResponseMessage<BookResponse> response = bookService.updateBook(bookId, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    //TODO ödünç verilmiş kitap için de test et.
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{book_Id}")
    public ResponseMessage<BookResponse> deleteBook(@PathVariable Long book_Id) {
       return bookService.deleteBookById(book_Id);

    }

}
