package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wahdini.getajobcopy.model.Job;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobRepository;
import wahdini.getajobcopy.repository.UserRepository;

@Controller
@RequestMapping("/job")
public class DetailPekerjaanController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // === DETAIL PEKERJAAN ===
    @GetMapping("/{id}")
    public String showJobDetail(@PathVariable Long id, Model model) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job tidak ditemukan"));

        User postedBy = job.getUser();

        model.addAttribute("job", job);
        model.addAttribute("postedBy", postedBy);

        return "jobdetail";
    }

    // === APPLY PEKERJAAN ===
    @PostMapping("/{id}/apply")
    public String applyJob(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job tidak ditemukan"));

        String username = (String) session.getAttribute("username");
        User currentUser = userRepository.findByUsername(username);

        model.addAttribute("job", job);
        model.addAttribute("postedBy", job.getUser());
        model.addAttribute("successApply", "Lamaran berhasil dikirim!");

        return "jobdetail";
    }

    
}
