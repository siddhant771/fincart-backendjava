package fincart.repository;


import fincart.entity.Expense;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    Page<Expense> findAllByUserEmail(
            String email, Pageable pageable);

    Page<Expense> findAllByUserEmailAndCategoryIgnoreCase(
            String email, String category, Pageable pageable);

    Optional<Expense> findByIdAndUserEmail(
            Long id, String email);
}