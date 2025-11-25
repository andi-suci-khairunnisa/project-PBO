package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.JobRepository;
import wahdini.getajobcopy.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/job")
public class DetailPekerjaanController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    // === DETAIL PEKERJAAN ===
    @GetMapping("/{id}")
    public String showJobDetail(@PathVariable Long id, Model model) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job tidak ditemukan"));

        model.addAttribute("job", job);
        model.addAttribute("postedBy", job.getUser());

        return "jobdetail";
    }

    // === APPLY PEKERJAAN ===
    @PostMapping("/{id}/apply")
    public String applyJob(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job tidak ditemukan"));

        String username = (String) session.getAttribute("username");
        User currentUser = userRepository.findByUsername(username);

        // Cegah double apply
        List<JobApplication> existing = jobApplicationRepository.findByUserAndJob(currentUser, job);
        if (!existing.isEmpty()) {
            model.addAttribute("job", job);
            model.addAttribute("postedBy", job.getUser());
            model.addAttribute("successApply", "Anda sudah melamar pekerjaan ini.");
            return "jobdetail";
        }

        // Simpan lamaran
        JobApplication app = new JobApplication();
        app.setUser(currentUser);
        app.setJob(job);
        app.setStatus("APPLIED");
        app.setAppliedDate(LocalDateTime.now());
        jobApplicationRepository.save(app);

        model.addAttribute("job", job);
        model.addAttribute("postedBy", job.getUser());
        model.addAttribute("successApply", "Lamaran berhasil dikirim!");

        return "jobdetail";
    }

    // === HALAMAN JOB BOARD DETAIL (DAFTAR PELAMAR UNTUK PEMILIK) ===
    @GetMapping("/{id}/jobboarddetail")
    public String showJobBoardDetail(@PathVariable Long id, Model model, HttpSession session) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job tidak ditemukan"));

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User currentUser = userRepository.findByUsername(username);

        // Hanya pemilik pekerjaan boleh melihat list pelamar
        if (currentUser == null || !job.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/";
        }

        java.util.List<JobApplication> applicants = jobApplicationRepository.findByJob(job);

        // tandai bahwa pemilik telah membuka halaman detail pelamar -> hapus notifikasi
        job.setLastViewedAt(LocalDateTime.now());
        jobRepository.save(job);

        model.addAttribute("job", job);
        model.addAttribute("applicants", applicants);

        return "jobboarddetail";
    }

    @PostMapping("/{jobId}/applicants/{appId}/accept")
    public String acceptApplicant(@PathVariable Long jobId, @PathVariable Long appId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User currentUser = userRepository.findByUsername(username);

        JobApplication app = jobApplicationRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Lamaran tidak ditemukan"));

        if (!app.getJob().getId().equals(jobId)) return "redirect:/";
        if (!app.getJob().getUser().getId().equals(currentUser.getId())) return "redirect:/";

        app.setStatus("ACCEPTED");
        jobApplicationRepository.save(app);

        // Tandai job sebagai assigned/selesai sehingga tidak tampil di job board
        Job job = app.getJob();
        job.setStatus("ASSIGNED");
        jobRepository.save(job);

        return "redirect:/job/" + jobId + "/jobboarddetail";
    }

    @PostMapping("/{jobId}/applicants/{appId}/reject")
    public String rejectApplicant(@PathVariable Long jobId, @PathVariable Long appId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User currentUser = userRepository.findByUsername(username);

        JobApplication app = jobApplicationRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Lamaran tidak ditemukan"));

        if (!app.getJob().getId().equals(jobId)) return "redirect:/";
        if (!app.getJob().getUser().getId().equals(currentUser.getId())) return "redirect:/";

        app.setStatus("REJECTED");
        jobApplicationRepository.save(app);

        return "redirect:/job/" + jobId + "/jobboarddetail";
    }
}
