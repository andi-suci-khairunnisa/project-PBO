package wahdini.getajobcopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wahdini.getajobcopy.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
