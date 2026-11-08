package fincart.controller;


import fincart.service.AuthService;
import fincart.service.ExpenseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final AuthService auth;
    private final ExpenseService expenses;

    public ApiController(AuthService auth, ExpenseService expenses) {
        this.auth = auth;
        this.expenses = expenses;
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthService.AuthResponse register(
            @RequestBody RegisterRequest request) {
        return auth.register(request.name(), request.email(),
                request.password());
    }

    @PostMapping("/auth/login")
    public AuthService.AuthResponse login(
            @RequestBody RegisterRequest request) {
        return auth.login(request.email(), request.password());
    }

    @PostMapping("/expenses")
    public Object create(Authentication authentication,
                         @RequestBody ExpenseService.ExpenseRequest request) {
        return expenses.create(authentication.getName(), request);
    }

    @GetMapping("/expenses")
    public Object getAll(Authentication authentication,
                         @RequestParam(required = false) String category,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        return expenses.getAll(authentication.getName(), category, page, size);
    }

    @GetMapping("/expenses/{id}")
    public Object getOne(Authentication authentication, @PathVariable Long id) {
        return expenses.getOne(authentication.getName(), id);
    }

    @PutMapping("/expenses/{id}")
    public Object update(Authentication authentication, @PathVariable Long id,
                         @RequestBody ExpenseService.ExpenseRequest request) {
        return expenses.update(authentication.getName(), id, request);
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        expenses.delete(authentication.getName(), id);
    }

    public record RegisterRequest(String name, String email, String password) {}
}