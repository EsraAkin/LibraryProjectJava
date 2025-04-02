package com.library.demo.service.businnes;

import com.library.demo.entity.businnes.Category;
import com.library.demo.exception.ResourceNotFoundException;
import com.library.demo.payload.messages.ErrorMessages;
import com.library.demo.repository.businnes.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));

    }

}
