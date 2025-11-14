package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // Menampilkan semua pekerjaan di halaman "pekerjaan terbaru"
    @GetMapping("/jobs")
    public String getAllJobs(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobs"; // akan membuat jobs.html
    }

    // Tambah pekerjaan baru (halaman form)
    @GetMapping("/jobs/add")
    public String showAddJobForm() {
        return "add-job";
    }

    // Simpan pekerjaan baru
    @PostMapping("/jobs/add")
    public String addJob(@RequestParam String title,
                         @RequestParam String location,
                         @RequestParam String price) {

        Job job = new Job(title, location, price);
        jobRepository.save(job);

        return "redirect:/jobs"; // kembali ke daftar pekerjaan
    }

    // API sederhana untuk dashboard (opsional)
    @GetMapping("/api/jobs")
    @ResponseBody
    public java.util.List<Job> getJobsAPI() {
        return jobRepository.findAll();
    }
}
