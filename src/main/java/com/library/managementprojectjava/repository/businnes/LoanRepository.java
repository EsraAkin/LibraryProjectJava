package com.library.managementprojectjava.repository.businnes;

import com.library.managementprojectjava.entity.businnes.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserIdAndReturnedFalse(Long userId);
    long countByUserIdAndReturnedFalse(Long userId);

   // Page<Loan> findByUserId(Long userId, Pageable pageable);

    Page<Loan> findByBookId(Long bookId, Pageable pageable);

    Page<Loan> findByUserId(Long id, Pageable pageable);

    long countByReturnDateIsNull();

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.expireDate < CURRENT_DATE AND l.returnDate IS NULL")
    long countExpiredBooks();



}
