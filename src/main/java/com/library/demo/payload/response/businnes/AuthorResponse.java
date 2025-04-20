package com.library.demo.payload.response.businnes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorResponse {
    private Long id;

    private String name;

    private Boolean builtIn;
}
