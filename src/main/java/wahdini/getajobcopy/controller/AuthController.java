package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(@RequestParam(value = "error", required = false) String error, org.springframework.ui.Model model) {
        if (error != null) {
            model.addAttribute("error", "Username atau password salah. Silakan coba lagi.");
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {

            // WAJIB AGAR /pekerjaansaya TIDAK NULL
            session.setAttribute("loggedInUser", user);

            // Yang lama tetap boleh
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userId", user.getId());

            return "redirect:/dashboard";
        }

        return "redirect:/?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
