package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Category;
import com.library.demo.entity.businnes.Publisher;
import com.library.demo.exception.ConflictException;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.mappers.CategoryMappers;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.payload.messages.SuccessMessages;
import com.library.demo.payload.request.businnes.CategoryResponse;
import com.library.demo.payload.response.businnes.CategoryRequest;
import com.library.demo.payload.response.businnes.PublisherResponse;
import com.library.demo.payload.response.businnes.ResponseMessage;
import com.library.demo.repository.businnes.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMappers categoryMappers;

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));

    }

    public ResponseMessage<CategoryResponse> saveCategory(@Valid CategoryRequest categoryRequest) {

        //unique controll
        if (categoryRepository.existsBySequence(categoryRequest.getSequence())) {
            throw new ConflictException(ErrorMessages.CATEGORY_ALREADY_MESSAGE_SEQUENCE);
        }

        if (categoryRepository.existsByNameIgnoreCase(categoryRequest.getName())) {
            throw new ConflictException(ErrorMessages.CATEGORY_ALREADY_MESSAGE_NAME);
        }


        Category categoryToSave = categoryRepository.save(categoryMappers.mapCategoryRequestToCategory(categoryRequest));
        return ResponseMessage.<CategoryResponse>builder()
                .message(SuccessMessages.CATEGORY_CREATE)
                .returnBody(categoryMappers.mapCategoryToCategoryResponse(categoryToSave))
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    public Page<CategoryResponse> pageableCategory(int page, int size, String sort, String type) {
        Sort.Direction direction = type.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMappers::mapCategoryToCategoryResponse);
    }

    public ResponseMessage<CategoryResponse> getCategoryByIdRes(Long categoryId) {
        return ResponseMessage.<CategoryResponse>builder()
                .message(SuccessMessages.CATEGORY_FOUND)
                .returnBody(categoryMappers.mapCategoryToCategoryResponse(getCategoryById(categoryId)))
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
