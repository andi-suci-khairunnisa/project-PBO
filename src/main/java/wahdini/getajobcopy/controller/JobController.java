package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.repository.UserRepository;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

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

    // === LIST JOB HALAMAN PEKERJAAN TERBARU ===
    @GetMapping
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs";
    }

    // === FORM TAMBAH PEKERJAAN ===
    @GetMapping("/add")
    public String showAddJobForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username);

        java.util.List<Job> jobs = jobRepository.findByUser(user);

        // determine which jobs have NEW applicants since owner last viewed
        java.util.Set<Long> jobsWithApplicants = new java.util.HashSet<>();
        for (Job j : jobs) {
            java.util.List<JobApplication> apps = jobApplicationRepository.findByJob(j);
            if (apps.isEmpty()) continue;

            // find latest application date
            java.time.LocalDateTime latest = null;
            for (JobApplication a : apps) {
                if (a.getAppliedDate() != null) {
                    if (latest == null || a.getAppliedDate().isAfter(latest)) {
                        latest = a.getAppliedDate();
                    }
                }
            }

            // if owner never viewed (lastViewedAt == null) and there is at least one app -> show dot
            if (j.getLastViewedAt() == null && latest != null) {
                jobsWithApplicants.add(j.getId());
            } else if (latest != null && j.getLastViewedAt() != null && latest.isAfter(j.getLastViewedAt())) {
                jobsWithApplicants.add(j.getId());
            }
        }

        model.addAttribute("jobs", jobs); // ⬅ kirim list job ke halaman
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

    // === API LIST JOB ===
    @GetMapping("/api")
    @ResponseBody
    public java.util.List<Job> getJobsAPI() {
        return jobRepository.findAll();
    }
    
}
