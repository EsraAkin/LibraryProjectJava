package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Category;
import com.library.demo.payload.request.businnes.CategoryResponse;
import com.library.demo.payload.response.businnes.CategoryRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Builder
public class CategoryMappers {


    public Category mapCategoryRequestToCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName())
                .builtIn(categoryRequest.getBuiltIn())
                .sequence(categoryRequest.getSequence())
                .build();
    }

    public CategoryResponse mapCategoryToCategoryResponse(Category category) {
        return CategoryResponse.
                builder()
                .id(category.getId())
                .name(category.getName())
                .builtIn(category.getBuiltIn())
                .sequence(category.getSequence())
                .build();

    }

}
