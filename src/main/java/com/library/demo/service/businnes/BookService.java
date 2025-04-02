package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Author;
import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Category;
import com.library.demo.entity.businnes.Publisher;
import com.library.demo.payload.mappers.BookMappers;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.service.validator.UniquePropertyValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMappers bookMappers;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final UniquePropertyValidator uniquePropertyValidator;


    public ResponseMessage<BookResponse> saveBook(@Valid BookRequest bookRequest) {
        Category category = categoryService.getCategoryById(bookRequest.getCategory());
        Author author = authorService.getAuthorById(bookRequest.getAuthor());
        Publisher publisher = publisherService.getPublisherById(bookRequest.getPublisher());

        //validate unique control
        uniquePropertyValidator.checkDublication(bookRequest.getIsbn());

        //map DTO to Entity
        Book book = bookMappers.mapBookRequestToBook(bookRequest, author, publisher, category);

        //save
        Book savedBook = bookRepository.save(book);

        //map Entity to BookResponse
        return ResponseMessage.<BookResponse>builder()
                .returnBody(bookMappers.mapBookToBookResponse(savedBook))
                .message(SuccessMessages.BOOK_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();


    }



    public Page<BookResponse> getBooks(String q, Long cat, Long author, Long publisher, Pageable pageable) {

        Page<Book> books = bookRepository.searchBooks(q, cat, author, publisher,  pageable);
        return books.map(bookMappers::mapBookToBookResponse);
    }

}
