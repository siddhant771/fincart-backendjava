package fincart.service;

import fincart.entity.User;
import fincart.repository.UserRepository;
import fincart.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder encoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(String name, String email, String password) {
        if (repository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(name, email, encoder.encode(password));
        repository.save(user);

        return new AuthResponse(user.getId(), user.getEmail(),
                jwtService.generateToken(user.getEmail()));
    }

    public AuthResponse login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse(user.getId(), user.getEmail(),
                jwtService.generateToken(user.getEmail()));
    }

    public record AuthResponse(Long userId, String email, String token) {}
}