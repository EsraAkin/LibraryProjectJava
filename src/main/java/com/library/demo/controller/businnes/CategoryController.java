package com.library.demo.controller.businnes;

import com.library.demo.payload.request.businnes.CategoryResponse;
import com.library.demo.payload.response.businnes.CategoryRequest;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.service.businnes.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/categories")
    public ResponseMessage<CategoryResponse> saveCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.saveCategory(categoryRequest);
    }

}
