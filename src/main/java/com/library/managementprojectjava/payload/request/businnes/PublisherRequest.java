package com.library.managementprojectjava.payload.request.businnes;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PublisherRequest {


    @NotNull(message = "Publisher Name is required")
    @Size(min = 2, max = 50, message = "Publisher Name should be minimum:2 max:50 characters")
    private String name;

    private Boolean builtIn;

}
