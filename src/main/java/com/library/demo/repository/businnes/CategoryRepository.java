package com.library.demo.repository.businnes;

import com.library.demo.entity.businnes.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySequence(int sequence);

    boolean existsByNameIgnoreCase(String name);
}



