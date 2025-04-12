package com.library.demo.repository.businnes;

import com.library.demo.entity.businnes.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);



    @Query("""
    SELECT b FROM Book b
    WHERE 
        ( :q IS NOT NULL OR :cat IS NOT NULL OR :author IS NOT NULL OR :publisher IS NOT NULL )
        AND ( :q IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :q, '%'))
                      OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :q, '%'))
                      OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :q, '%'))
                      OR LOWER(b.publisher.name) LIKE LOWER(CONCAT('%', :q, '%')) )
        AND ( :cat IS NULL OR b.category.id = :cat )
        AND ( :author IS NULL OR b.author.id = :author )
        AND ( :publisher IS NULL OR b.publisher.id = :publisher )
""")
    Page<Book> searchBooks(
            @Param("q") String q,
            @Param("cat") Long cat,
            @Param("author") Long author,
            @Param("publisher") Long publisher,
            Pageable pageable
    );




}
