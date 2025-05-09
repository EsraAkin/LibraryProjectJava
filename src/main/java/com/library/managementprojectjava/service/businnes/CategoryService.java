package com.library.managementprojectjava.service.businnes;

import com.library.managementprojectjava.entity.businnes.Category;
import com.library.managementprojectjava.exception.ConflictException;
import com.library.managementprojectjava.exception.ResourceNotFoundException;
import com.library.managementprojectjava.payload.mappers.CategoryMappers;
import com.library.managementprojectjava.payload.messages.ErrorMessages;
import com.library.managementprojectjava.payload.messages.SuccessMessages;
import com.library.managementprojectjava.payload.request.businnes.CategoryResponse;
import com.library.managementprojectjava.payload.response.businnes.CategoryRequest;
import com.library.managementprojectjava.payload.response.businnes.ResponseMessage;
import com.library.managementprojectjava.repository.businnes.CategoryRepository;
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
            throw new ConflictException(ErrorMessages.CATEGORY_SEQUENCE_ALREADY_EXISTS_MESSAGE);
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

    public ResponseMessage<CategoryResponse> updateCategory(@Valid CategoryRequest categoryRequest, Long categoryId) {

        //exist DB category
        Category existingCategory = getCategoryById(categoryId);

        //is BuiltIn
        if (Boolean.TRUE.equals(existingCategory.getBuiltIn())) {
            throw new ConflictException(ErrorMessages.CATEGORY_NOT_PERMITTED_METHOD_MESSAGE);
        }

        //name unique validation
        if (!existingCategory.getName().equalsIgnoreCase(categoryRequest.getName()) &&
                categoryRepository.equals(categoryRequest.getName())) {
            throw new ConflictException(ErrorMessages.CATEGORY_ALREADY_MESSAGE_NAME);

        }

        //sequence unique validation
        if (existingCategory.getSequence() != categoryRequest.getSequence() &&
                categoryRepository.existsBySequence(categoryRequest.getSequence())) {
            throw new ConflictException(ErrorMessages.CATEGORY_SEQUENCE_ALREADY_EXISTS_MESSAGE);
        }

        //update
        existingCategory.setName(categoryRequest.getName());
        existingCategory.setSequence(categoryRequest.getSequence());
        existingCategory.setId(existingCategory.getId());

        //save
        Category updatedCategory = categoryRepository.save(existingCategory);


        //return
        return ResponseMessage.<CategoryResponse>builder()
                .message(SuccessMessages.CATEGORY_UPDATE)
                .returnBody(categoryMappers.mapCategoryToCategoryResponse(updatedCategory))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<CategoryResponse> deleteCategory(Long categoryId) {
        //exist Category DB
        Category existingCategory = getCategoryById(categoryId);

        // If there are books attached to the category, it cannot be deleted
        if (existingCategory.getBooks() != null && !existingCategory.getBooks().isEmpty()) {
            throw new ConflictException("This category cannot be deleted because it has associated books.");
        }
        categoryRepository.deleteById(categoryId);
        return ResponseMessage.<CategoryResponse>builder()
                .message(SuccessMessages.CATEGORY_DELETE)
                .returnBody(categoryMappers.mapCategoryToCategoryResponse(existingCategory))
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
