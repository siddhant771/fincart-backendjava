package fincart.service;


import fincart.entity.Expense;
import fincart.entity.User;
import fincart.repository.ExpenseRepository;
import fincart.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExpenseService {

    private final ExpenseRepository expenses;
    private final UserRepository users;

    public ExpenseService(ExpenseRepository expenses,
                          UserRepository users) {
        this.expenses = expenses;
        this.users = users;
    }

    public Expense create(String email, ExpenseRequest request) {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenses.save(new Expense(
                request.title(), request.amount(),
                request.category(), request.date(), user));
    }

    public Page<Expense> getAll(
            String email, String category, int page, int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("expenseDate").descending());

        if (category == null || category.isBlank()) {
            return expenses.findAllByUserEmail(email, pageable);
        }

        return expenses.findAllByUserEmailAndCategoryIgnoreCase(
                email, category, pageable);
    }

    public Expense getOne(String email, Long id) {
        return expenses.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public Expense update(String email, Long id, ExpenseRequest request) {
        Expense expense = getOne(email, id);

        expense.update(request.title(), request.amount(),
                request.category(), request.date());

        return expenses.save(expense);
    }

    public void delete(String email, Long id) {
        expenses.delete(getOne(email, id));
    }

    public record ExpenseRequest(
            String title,
            BigDecimal amount,
            String category,
            LocalDate date) {}
}