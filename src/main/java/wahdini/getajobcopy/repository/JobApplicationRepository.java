package wahdini.getajobcopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.model.Job;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUser(User user);

    List<JobApplication> findByUserAndStatus(User user, String status);

    List<JobApplication> findByUserAndJob(User user, Job job); // untuk cegah double apply

    List<JobApplication> findByJob(Job job);

    boolean existsByUserAndJob(User user, Job job);

}

/*
 * JobApplicationRepository (Interface Repository)
 * Tanggung Jawab: Menyediakan akses data untuk entity JobApplication guna mengelola proses aplikasi pekerjaan.
 * SOLID Principles:
 *   - Interface Segregation: Interface ini fokus hanya pada operasi JobApplication, tidak membawa method yang tidak perlu.
 *   - Dependency Inversion: Menggunakan abstraksi JpaRepository, tidak terikat implementasi spesifik.
 * Method tersedia:
 *   - findByUser(): Ambil semua lamaran/aplikasi yang dibuat oleh user tertentu
 *   - findByUserAndStatus(): Filter lamaran berdasarkan status (APPLIED/ACCEPTED/REJECTED/FINISHED)
 *   - findByUserAndJob(): Cegah user apply dua kali ke job yang sama
 *   - findByJob(): Ambil semua pelamar yang melamar ke suatu job (untuk job owner lihat pelamar)
 *   - existsByUserAndJob(): Cek keberadaan lamaran dengan efficient boolean check tanpa load full object
 */
