package com.library.demo.payload.request.businnes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {

    @NotNull(message = "Name must not be empty!")
    @Size(min = 2, max = 80, message = "Book name should be at least 2 characters")
    private String name;

    @NotNull(message = "Isbn must not be empty!")
    @Size(min = 17, max = 17, message = "shelfCode should be 17 characters")
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d{1}", message = "ISBN format should be 999-99-99999-99-9")
    private String isbn;

    private Integer pageCount; //I have to use Integer

    private Integer publishDate;  //I have to use Integer

    private String image;

    @NotNull(message = "ShelfCode must not be empty!")
    @Size(min = 6, max = 6, message = "shelfCode should be 6 characters")
    @Pattern(regexp = "^[A-Z]{2}-\\d{3}$", message = "shelfCode should be AA-999")
    private String shelfCode;

    @NotNull
    private boolean active;

    @NotNull
    private boolean featured;

    @NotNull
    private Long author;

    @NotNull
    private Long publisher;


    private Long category;



}
