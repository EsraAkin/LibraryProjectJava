package com.library.demo.entity.businnes;

import com.library.demo.entity.user.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String isbn;

    private Integer pageCount; //I have to use Integer

    private Integer publishDate;  //I have to use Integer

    private String image;

    private boolean loanable = true;

    private String shelfCode;

    private boolean active = true;

    private boolean featured = false;

    private LocalDateTime createDate;

    private boolean builtIn = false;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
