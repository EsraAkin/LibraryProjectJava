package com.library.managementprojectjava.payload.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularUserResponse {
    private Long id;
    private String name;
}