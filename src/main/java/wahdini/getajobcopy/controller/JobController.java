package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.repository.UserRepository;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    // === LIST JOB DI HALAMAN "PEKERJAAN TERBARU" ===
    @GetMapping
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs";
    }

    // === FORM TAMBAH PEKERJAAN ===
    @GetMapping("/add")
    public String showAddJobForm() {
        return "tambahpekerjaan";
    }

    // === SIMPAN PEKERJAAN BARU ===
    @PostMapping("/add")
    public String addJob(
            @RequestParam String title,
            @RequestParam String location,
            @RequestParam String price,
            @RequestParam(required = false) String description,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username);

        Job job = new Job(title, location, price, description, user);
        jobRepository.save(job);

        model.addAttribute("successMessage", "Pekerjaan berhasil ditambahkan!");
        return "tambahpekerjaan";
    }

    // === API LIST JOB ===
    @GetMapping("/api")
    @ResponseBody
    public java.util.List<Job> getJobsAPI() {
        return jobRepository.findAll();
    }
}
