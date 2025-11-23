package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
private JobRepository jobRepository;

@GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model) {

    String username = (String) session.getAttribute("username");
    if (username == null) return "redirect:/";

    model.addAttribute("username", username);

    // Tambahkan list job terbaru
    model.addAttribute("jobs", jobRepository.findAll());

    return "dashboard";
}

// @GetMapping("/tambahpekerjaan")
// public String showAddJobForm() {
//     return "tambahpekerjaan";
// }

@GetMapping("/tambahpekerjaan")
public String redirectAddJob() {
    return "redirect:/jobs/add";
}
}
