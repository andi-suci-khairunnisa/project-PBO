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

    boolean existsByUserAndJob(User user, Job job);

}
