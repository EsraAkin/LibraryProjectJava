package com.library.demo.payload.response.businnes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;

    private String name;

    private String isbn;

    private Integer pageCount;

    private Integer publishDate;

    private String image;

    private String shelfCode;

    private boolean active;

    private boolean featured;

    private Long author;

    private Long publisher;

    private Long category;

    private String createDate;

}
