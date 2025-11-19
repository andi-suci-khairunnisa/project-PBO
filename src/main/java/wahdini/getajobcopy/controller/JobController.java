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
public class JobController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    // Menampilkan semua pekerjaan di halaman "pekerjaan terbaru"
    @GetMapping("/jobs")
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs"; // akan membuat jobs.html
    }

    // Tambah pekerjaan baru (halaman form)
    // Tambah pekerjaan baru (halaman form)
    @GetMapping("/jobs/add")
    public String showAddJobForm() {
        return "tambahpekerjaan";
    }

    // Simpan pekerjaan baru
    @PostMapping("/jobs/add")
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
        model.addAttribute("jobs", jobRepository.findAll()); // opsional
        return "tambahpekerjaan";  // tetap di halaman ini
    }




    // API sederhana untuk dashboard (opsional)
    @GetMapping("/api/jobs")
    @ResponseBody
    public java.util.List<Job> getJobsAPI() {
        return jobRepository.findAll();
    }

    @GetMapping("/job/{id}")
    public String viewJobDetail(@PathVariable Long id, Model model) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Job ID"));

        User user = job.getUser();

        model.addAttribute("job", job);
        model.addAttribute("postedBy", user);

        return "jobdetail"; // nama file HTML
    }

}
