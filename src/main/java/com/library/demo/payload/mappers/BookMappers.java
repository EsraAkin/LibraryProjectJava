package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Author;
import com.library.demo.entity.businnes.Book;
import com.library.demo.entity.businnes.Category;
import com.library.demo.entity.businnes.Publisher;
import com.library.demo.payload.request.businnes.BookRequest;
import com.library.demo.payload.response.businnes.BookResponse;
import org.springframework.stereotype.Component;

@Component
public class BookMappers {

    public Book mapBookRequestToBook(BookRequest bookRequest, Author author, Publisher publisher, Category category) {
        return Book.builder()
                .name(bookRequest.getName())
                .isbn(bookRequest.getIsbn())
                .pageCount(bookRequest.getPageCount())
                .author(author)
                .publisher(publisher)
                .publishDate(bookRequest.getPublishDate())
                .category(category)
                .image(bookRequest.getImage())
                .shelfCode(bookRequest.getShelfCode())
                .active(bookRequest.isActive())
                .featured(bookRequest.isFeatured())
                .loanable(true) // Kullanıcıdan gelmediği için biz otomatik ayarlıyoruz
                .build();

    }


    public BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .author(book.getAuthor().getId())
                .publisher(book.getPublisher().getId())
                .publishDate(book.getPublishDate())
                .category(book.getCategory().getId())
                .image(book.getImage())
                .shelfCode(book.getShelfCode())
                .active(book.isActive())
                .featured(book.isFeatured())
                .createDate(book.getCreateDate() != null ? book.getCreateDate().toString() : "Date null")

                .build();
    }

//TODO 2 tane response var. düzenleme gerek

    public BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .publishDate(book.getPublishDate())
                .image(book.getImage())
                .shelfCode(book.getShelfCode())
                .active(book.isActive())
                .featured(book.isFeatured())
                .author(book.getAuthor().getId())
                .publisher(book.getPublisher().getId())
                .category(book.getCategory().getId())
                .createDate(book.getCreateDate().toString())
                .build();
    }


}
