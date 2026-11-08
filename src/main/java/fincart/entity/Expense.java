package fincart.entity;



import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Expense() {}

    public Expense(String title, BigDecimal amount,
                   String category, LocalDate date, User user) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.expenseDate = date;
        this.user = user;
    }

    public void update(String title, BigDecimal amount,
                       String category, LocalDate date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.expenseDate = date;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getExpenseDate() { return expenseDate; }
}