package wahdini.getajobcopy.service;

import org.springframework.stereotype.Service;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User register(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return userRepository.save(newUser);
    }
}

/*
 * AuthService (Service Layer)
 * Tanggung Jawab: Mengenkapsulasi logika autentikasi dan registrasi user, memisahkan business logic dari controller.
 * SOLID Principles:
 *   - Single Responsibility: Service ini fokus hanya pada operasi auth (login & register), tidak menangani HTTP request/response.
 *   - Dependency Inversion: Constructor injection untuk UserRepository, tidak terikat implementasi konkret.
 * Method tersedia:
 *   - authenticate(): Validasi username & password, return User jika kredensial benar, null jika salah.
 *   - register(): Buat user baru dan simpan ke database, return user yang baru dibuat.
 */
