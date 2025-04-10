package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Author;
import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Category;
import com.library.demo.entity.businnes.Publisher;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.BookMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.BookRepository;
import com.library.demo.service.helper.MethodHelper;
import com.library.demo.service.validator.BookUniquePropertyValidator;
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
    private final BookUniquePropertyValidator uniquePropertyValidator;
    private final MethodHelper methodHelper;


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

        Page<Book> books = bookRepository.searchBooks(q, cat, author, publisher, pageable);
        return books.map(bookMappers::mapBookToBookResponse);
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.BOOK_NOT_FOUND));

    }

    public ResponseMessage<BookResponse> getBookById(Long bookId) {
        return ResponseMessage.<BookResponse>builder()
                .returnBody(bookMappers.mapBookToBookResponse(findBookById(bookId)))
                .message(SuccessMessages.BOOK_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<BookResponse> updateBook(Long bookId, BookRequest bookRequest) {
        Category category = categoryService.getCategoryById(bookRequest.getCategory());
        Author author = authorService.getAuthorById(bookRequest.getAuthor());
        Publisher publisher = publisherService.getPublisherById(bookRequest.getPublisher());

        //exist Book in DB
        Book bookFromDB = findBookById(bookId);

        //validate unique prop
        uniquePropertyValidator.checkUniqueProperty(bookFromDB, bookRequest);

        //mapping
        Book bookToSave = bookMappers.mapBookRequestToBook(bookRequest, author, publisher, category);
        bookToSave.setId(bookId);

        Book savedBook = bookRepository.save(bookToSave);
        return ResponseMessage.<BookResponse>builder()
                .returnBody(bookMappers.mapBookToBookResponse(savedBook))
                .message(SuccessMessages.BOOK_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<BookResponse> deleteBookById(Long bookId) {

        Book book = findBookById(bookId);

        methodHelper.validateBookCanBeDeleted(book);

        bookRepository.delete(book);

        return ResponseMessage.<BookResponse>builder()
                .message(SuccessMessages.BOOK_DELETE)
                .httpStatus(HttpStatus.OK)
                .returnBody(bookMappers.mapBookToBookResponse(book))
                .build();


    }
}
