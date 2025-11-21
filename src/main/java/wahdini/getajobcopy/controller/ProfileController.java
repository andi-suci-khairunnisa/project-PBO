package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        // Ambil user dari database
        User me = userRepository.findByUsername(username);

        model.addAttribute("user", me);

        return "profile";
    }
}
