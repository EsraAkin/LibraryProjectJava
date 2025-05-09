package com.library.managementprojectjava.payload.request.businnes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorRequest {

    @NotNull(message = "Author Name is required")
    @Size(min = 4, max = 70, message = "Author Name should be minimum:4 max:70 characters")
    private String name;

    private Boolean builtIn;
}
