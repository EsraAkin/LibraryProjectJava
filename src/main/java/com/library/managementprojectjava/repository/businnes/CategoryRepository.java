package com.library.managementprojectjava.repository.businnes;

import com.library.managementprojectjava.entity.businnes.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySequence(int sequence);

    boolean existsByNameIgnoreCase(String name);
}



