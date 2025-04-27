package com.library.managementprojectjava.payload.response.businnes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublisherResponse {

    private Long id;

    private String name;

    private Boolean builtIn;

}
