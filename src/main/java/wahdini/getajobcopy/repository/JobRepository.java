package wahdini.getajobcopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wahdini.getajobcopy.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Ambil semua job berdasarkan user
    java.util.List<Job> findByUser(wahdini.getajobcopy.model.User user);
}
