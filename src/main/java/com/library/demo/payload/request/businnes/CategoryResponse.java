package com.library.demo.payload.request.businnes;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private Boolean builtIn;

    private int sequence;
}
