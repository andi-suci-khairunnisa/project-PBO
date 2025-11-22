package wahdini.getajobcopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import wahdini.getajobcopy.model.JobApplication;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import java.util.List;

@Controller
public class JobApplicationController {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @GetMapping("/pekerjaansaya")
    public String pekerjaanSaya(Model model, HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            System.out.println("⚠️ USER SESSION NULL — redirect login");
            return "redirect:/login";
        }

        System.out.println("LOGIN USER ID = " + user.getId());

        List<JobApplication> dilamar = jobApplicationRepository.findByUserAndStatus(user, "APPLIED");
        List<JobApplication> diterima = jobApplicationRepository.findByUserAndStatus(user, "ACCEPTED");
        List<JobApplication> selesai = jobApplicationRepository.findByUserAndStatus(user, "FINISHED");

        System.out.println("Jumlah Dilamar = " + dilamar.size());
        System.out.println("Jumlah Diterima = " + diterima.size());
        System.out.println("Jumlah Selesai = " + selesai.size());

        model.addAttribute("dilamarList", dilamar);
        model.addAttribute("diterimaList", diterima);
        model.addAttribute("selesaiList", selesai);

        return "pekerjaansaya";
    }

}
