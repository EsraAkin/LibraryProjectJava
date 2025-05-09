package com.library.managementprojectjava.controller.businnes;

import com.library.managementprojectjava.payload.request.businnes.CategoryResponse;
import com.library.managementprojectjava.payload.response.businnes.CategoryRequest;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.service.businnes.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseMessage<CategoryResponse> saveCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.saveCategory(categoryRequest);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> pageableCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String type
    ) {
        Page<CategoryResponse> responses = categoryService.pageableCategory(page, size, sort, type);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{categoryId}")
    public ResponseMessage<CategoryResponse> getByIdCategory(@PathVariable Long categoryId){
        return categoryService.getCategoryByIdRes(categoryId);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseMessage<CategoryResponse> updateCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                            @PathVariable Long categoryId){
        return categoryService.updateCategory(categoryRequest, categoryId);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseMessage<CategoryResponse> deleteCategory(@PathVariable Long categoryId){
        return categoryService.deleteCategory(categoryId);

    }


}
