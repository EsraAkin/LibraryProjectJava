package com.library.demo.payload.response.businnes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryRequest {

    @NotNull(message = "Category Name is required")
    @Size(min=2, max=80, message = "Category Name should be minimum:2 max:80 characters")
    private String name;

    private Boolean builtIn;

    @NotNull(message = "Sequence is required")
    private int sequence;
}
