package com.library.managementprojectjava.repository.businnes;

import com.library.managementprojectjava.entity.businnes.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
