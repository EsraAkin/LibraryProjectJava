package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping("/save")
    public ResponseMessage<BookResponse> saveBook(@RequestBody @Valid BookRequest bookRequest) {
        return bookService.saveBook(bookRequest);
    }

    @GetMapping("/books")
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



    @DeleteMapping("/delete/{book_Id}")
    public ResponseMessage<BookResponse> deleteBook(@PathVariable Long book_Id) {
        //return bookService.deleteBookById(book_Id);
        return null;
    }

}
