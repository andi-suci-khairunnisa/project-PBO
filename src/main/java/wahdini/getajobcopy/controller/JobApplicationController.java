package wahdini.getajobcopy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.JobRepository;
import java.util.List;

@Controller
public class JobApplicationController {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;

    public JobApplicationController(JobApplicationRepository jobApplicationRepository,
                                     JobRepository jobRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("/pekerjaansaya")
    public String pekerjaanSaya(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        List<JobApplication> dilamar = jobApplicationRepository.findByUserAndStatus(user, "APPLIED");
        List<JobApplication> diterima = jobApplicationRepository.findByUserAndStatus(user, "ACCEPTED");
        List<JobApplication> selesai = jobApplicationRepository.findByUserAndStatus(user, "FINISHED");

        model.addAttribute("dilamarList", dilamar);
        model.addAttribute("diterimaList", diterima);
        model.addAttribute("selesaiList", selesai);

        return "pekerjaansaya";
    }

    @PostMapping("/pekerjaansaya/{appId}/finish")
    public String markFinished(@PathVariable Long appId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        JobApplication app = jobApplicationRepository.findById(appId).orElse(null);
        if (app == null) return "redirect:/pekerjaansaya";

        // pastikan aplikasi milik user yang sedang login
        if (!app.getUser().getId().equals(user.getId())) return "redirect:/pekerjaansaya";

        app.setStatus("FINISHED");
        jobApplicationRepository.save(app);

        // juga tandai job sebagai FINISHED
        if (app.getJob() != null) {
            var job = app.getJob();
            job.setStatus("FINISHED");
            jobRepository.save(job);
        }

        return "redirect:/pekerjaansaya";
    }

}

// Controller ini menangani halaman "Pekerjaan Saya" untuk pekerja/pelamar:
// - Menampilkan daftar lamaran dalam berbagai status (APPLIED, ACCEPTED, FINISHED)
//   (`GET /pekerjaansaya`)
// - Menandai pekerjaan yang diterima sebagai FINISHED (`POST /pekerjaansaya/{appId}/finish`)
// Desain: menggunakan constructor injection untuk meningkatkan testabilitas dan
// mematuhi Dependency Inversion Principle. Debug logging telah dihapus untuk
// menjaga kode tetap bersih dan mudah dibaca.}
