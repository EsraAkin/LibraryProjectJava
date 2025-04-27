package com.library.managementprojectjava.repository.businnes;

import com.library.managementprojectjava.entity.businnes.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
