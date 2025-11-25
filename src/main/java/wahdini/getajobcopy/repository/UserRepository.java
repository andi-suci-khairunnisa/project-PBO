package wahdini.getajobcopy.repository;

import wahdini.getajobcopy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

/*
 * UserRepository (Interface Repository)
 * Tanggung Jawab: Menyediakan akses data untuk entity User, memfasilitasi pencarian user berdasarkan username.
 * SOLID Principles:
 *   - Interface Segregation: Interface ini fokus hanya pada operasi User, tidak membawa method yang tidak perlu.
 *   - Dependency Inversion: Bergantung pada abstraksi JpaRepository, bukan implementasi konkret.
 * Method tersedia:
 *   - findByUsername(): Cari user berdasarkan username (untuk proses login, registrasi, dan pencarian pengguna lain saat ingin chat/lihat profil)
 */
