package wahdini.getajobcopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wahdini.getajobcopy.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Ambil semua job berdasarkan user
    java.util.List<Job> findByUser(wahdini.getajobcopy.model.User user);

    // Ambil job berdasarkan status (mis. "ACTIVE")
    java.util.List<Job> findByStatus(String status);
}

/*
 * JobRepository (Interface Repository)
 * Tanggung Jawab: Menyediakan akses data untuk entity Job, memfasilitasi pencarian dan filtering pekerjaan.
 * SOLID Principles:
 *   - Interface Segregation: Interface ini fokus hanya pada operasi Job-related, tidak membawa method yang tidak perlu.
 *   - Dependency Inversion: Bergantung pada abstraksi JpaRepository, bukan implementasi konkret.
 * Method tersedia:
 *   - findByUser(): Ambil semua job yang dibuat oleh user tertentu (untuk halaman "Pekerjaan Saya")
 *   - findByStatus(): Filter job berdasarkan status (ACTIVE, CLOSED, dll) untuk menampilkan job board atau dashboard
 */
