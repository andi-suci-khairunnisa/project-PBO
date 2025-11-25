package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import wahdini.getajobcopy.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final JobRepository jobRepository;

    public DashboardController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        model.addAttribute("username", username);

        // Tambahkan list job terbaru — hanya tampilkan job yang masih ACTIVE
        model.addAttribute("jobs", jobRepository.findByStatus("ACTIVE"));

        return "dashboard";
    }

    @GetMapping("/tambahpekerjaan")
    public String redirectAddJob() {
        return "redirect:/jobs/add";
    }

}

// Controller ini bertanggung jawab untuk menyajikan halaman dashboard
// pengguna dan meneruskan pengguna ke halaman tambah pekerjaan.
// Tanggung jawab:
// - Memeriksa apakah pengguna ter-login (session username)
// - Menyediakan daftar pekerjaan berstatus "ACTIVE" untuk ditampilkan di dashboard
// - Mengarahkan endpoint `/tambahpekerjaan` ke `/jobs/add`
// Desain: controller hanya melakukan routing dan pengisian model; akses data
// didelegasikan ke `JobRepository` sehingga mematuhi Single Responsibility Principle.
