package wahdini.getajobcopy.controller;

import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final wahdini.getajobcopy.service.AuthService authService;

    public AuthController(wahdini.getajobcopy.service.AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "error", required = false) String error, org.springframework.ui.Model model) {
        if (error != null) {
            model.addAttribute("error", "Username atau password salah. Silakan coba lagi.");
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        authService.register(username, password);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        User user = authService.authenticate(username, password);
        if (user != null) {
            // Simpan user di session untuk akses halaman lain
            session.setAttribute("loggedInUser", user);
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

// Controller ini bertanggung jawab untuk autentikasi dan pendaftaran pengguna:
// - Menyajikan halaman login (`GET /`)
// - Menerima permintaan login (`POST /login`) dan menyimpan info user ke session
// - Menerima pendaftaran (`POST /register`) dan mendelegasikannya ke `AuthService`
// - Menangani logout (`GET /logout`)
// Perubahan: logika bisnis terkait autentikasi telah diekstraksi ke `AuthService`
// untuk mematuhi Single Responsibility Principle dan meningkatkan testabilitas.
