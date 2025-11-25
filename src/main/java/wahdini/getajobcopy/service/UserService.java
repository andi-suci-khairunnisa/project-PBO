package wahdini.getajobcopy.service;

import org.springframework.stereotype.Service;
import wahdini.getajobcopy.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

/*
 * UserService (Service Layer - Placeholder)
 * Tanggung Jawab: Reserved untuk user-related business logic di masa depan (validasi user, enrichment data, dll).
 * SOLID Principles:
 *   - Single Responsibility: Service ini dedicated untuk user operations, memisahkan concern dari controller.
 *   - Dependency Inversion: Constructor injection untuk UserRepository, fleksibel untuk extension.
 * 
 * Catatan: Saat ini UserService minimal karena operasi user sederhana (findByUsername sudah cukup di controller).
 * UserService dapat diperluas dengan method seperti updateProfile(), validateUsername(), dll di masa depan.
 */
