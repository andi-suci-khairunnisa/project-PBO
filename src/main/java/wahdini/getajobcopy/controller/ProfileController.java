package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;

@Controller
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        // Ambil user dari database
        User me = userRepository.findByUsername(username);

        model.addAttribute("user", me);
        model.addAttribute("isOwner", true);

        return "profile";
    }

    // View profile lain berdasarkan id (dipakai untuk melihat pelamar)
    @GetMapping("/profile/{id}")
    public String profileById(@PathVariable Long id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/";

        model.addAttribute("user", user);
        model.addAttribute("isOwner", isOwnerOfProfile(username, user));
        return "profile";
    }

    private boolean isOwnerOfProfile(String currentUsername, User profileUser) {
        return currentUsername != null && profileUser.getUsername() != null &&
               profileUser.getUsername().equals(currentUsername);
    }
}

// Controller ini menangani tampilan profil pengguna:
// - Menampilkan profil pengguna yang sedang login (`GET /profile`)
// - Menampilkan profil pengguna lain berdasarkan ID (`GET /profile/{id}`) —
//   digunakan untuk melihat profil pelamar saat pemilik job review lamaran
// - Set flag "isOwner" untuk menentukan apakah pengguna melihat profil mereka
//   sendiri atau orang lain (mempengaruhi tampilan UI: edit button, dll)
// Perbaikan SOLID: constructor injection dan helper method untuk mengurangi
// duplikasi kode dan meningkatkan readability.
