package com.library.managementprojectjava.payload.response.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularBookResponse {

    private Long id;
    private String name;
    private String isbn;
}