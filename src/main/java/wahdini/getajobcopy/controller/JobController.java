package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.repository.UserRepository;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobController(UserRepository userRepository,
                          JobRepository jobRepository,
                          JobApplicationRepository jobApplicationRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // === LAMAR PEKERJAAN ===
    @PostMapping("/apply/{jobId}")
    public String applyJob(@PathVariable Long jobId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            return "redirect:/jobs";
        }

        // Cek apakah sudah pernah melamar
        if (jobApplicationRepository.existsByUserAndJob(user, job)) {
            return "redirect:/jobdetail/" + jobId + "?alreadyApplied=true";
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");
        jobApplicationRepository.save(application);

        // Ubah status job menjadi selesai
        job.setStatus("SELESAI");
        jobRepository.save(job);

        return "redirect:/pekerjaansaya";
    }

    // === FORM TAMBAH PEKERJAAN ===
    @GetMapping("/add")
    public String showAddJobForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username);

        List<Job> jobs = jobRepository.findByUser(user);
        Set<Long> jobsWithApplicants = determineJobsWithNewApplicants(jobs);

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobsWithApplicants", jobsWithApplicants);
        return "tambahpekerjaan";
    }


    // === SIMPAN PEKERJAAN BARU ===
    @PostMapping("/add")
    public String addJob(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam String price,
            @RequestParam String duration,
            @RequestParam String kategori,
            @RequestParam String phone,
            @RequestParam String description,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username);

        Job job = new Job();
        job.setTitle(title);
        job.setLocation(location);
        job.setPrice(price);
        job.setDuration(duration);
        job.setKategori(kategori);
        job.setPhone(phone);
        job.setDescription(description);
        job.setUser(user);

        // otomatis isi posted date & status
        job.setPostedDate(LocalDateTime.now());
        job.setStatus("ACTIVE");

        jobRepository.save(job);

        return "redirect:/jobs/add?success=true";
    }

    // === HALAMAN PEKERJAAN SAYA ===
    @GetMapping("/my")
    public String pekerjaanSaya(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username);

        model.addAttribute("jobs", jobRepository.findByUser(user));
        return "pekerjaansaya";
    }

    private Set<Long> determineJobsWithNewApplicants(List<Job> jobs) {
        Set<Long> jobsWithApplicants = new HashSet<>();
        for (Job j : jobs) {
            List<JobApplication> apps = jobApplicationRepository.findByJob(j);
            if (apps.isEmpty()) continue;

            // find latest application date
            LocalDateTime latest = null;
            for (JobApplication a : apps) {
                if (a.getAppliedDate() != null) {
                    if (latest == null || a.getAppliedDate().isAfter(latest)) {
                        latest = a.getAppliedDate();
                    }
                }
            }

            // show dot if owner never viewed or latest app is newer than last viewed
            if (j.getLastViewedAt() == null && latest != null) {
                jobsWithApplicants.add(j.getId());
            } else if (latest != null && j.getLastViewedAt() != null && latest.isAfter(j.getLastViewedAt())) {
                jobsWithApplicants.add(j.getId());
            }
        }
        return jobsWithApplicants;
    }
}

// Controller ini menangani operasi terkait pekerjaan (job) oleh pemilik:
// - Menangani aplikasi/lamaran pekerjaan (`POST /jobs/apply/{jobId}`)
// - Menampilkan form tambah pekerjaan dan daftar job dengan notifikasi pelamar
//   (`GET /jobs/add`)
// - Menyimpan pekerjaan baru (`POST /jobs/add`)
// - Menampilkan daftar pekerjaan pengguna (`GET /jobs/my`)
// Perbaikan SOLID yang diterapkan:
// - Single Responsibility: logika notifikasi diekstraksi ke helper method
// - Constructor Injection: semua dependencies disuntikkan lewat konstruktor
// - Removed unused endpoints: /jobs dan /api dihapus (tidak dipakai)
