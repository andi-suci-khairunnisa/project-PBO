package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.UserRepository;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public GlobalModelAttributes(UserRepository userRepository,
                                  JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @ModelAttribute
    public void addNotifications(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return; // not logged in

        User me = userRepository.findByUsername(username);
        if (me == null) return;

        // count accepted job applications for this user (applicant perspective)
        int acceptedCount = jobApplicationRepository.findByUserAndStatus(me, "ACCEPTED").size();
        model.addAttribute("acceptedNotifications", acceptedCount);
    }
}

// @ControllerAdvice ini menyuntikkan atribut model global ke setiap response view:
// - Menghitung jumlah lamaran yang diterima (ACCEPTED) untuk pengguna yang sedang login
// - Menambahkan attribut "acceptedNotifications" ke model sehingga semua template
//   dapat mengakses notifikasi badge untuk lamaran yang diterima
// Desain: menggunakan constructor injection dan field final untuk meningkatkan
// testabilitas serta mematuhi Dependency Inversion Principle.
