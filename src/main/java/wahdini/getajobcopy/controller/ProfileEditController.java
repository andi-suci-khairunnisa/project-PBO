package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;

import java.io.File;
import java.io.IOException;

@Controller
public class ProfileEditController {

    @Autowired
    private UserRepository userRepository;

    // FORM EDIT
    @GetMapping("/profile/edit")
    public String editForm(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User me = userRepository.findByUsername(username);
        model.addAttribute("user", me);

        return "profile_edit";
    }

    // UPDATE PROFILE
    @PostMapping("/profile/update")
    public String updateProfile(
            @ModelAttribute User formUser,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session) throws IOException {

        String username = (String) session.getAttribute("username");
        User me = userRepository.findByUsername(username);

        // Update basic information
        me.setFullName(formUser.getFullName());
        me.setJob(formUser.getJob());
        me.setPhone(formUser.getPhone());
        me.setLocation(formUser.getLocation());
        me.setDescription(formUser.getDescription());

        // Update pengalaman kerja
        me.setExpTitle(formUser.getExpTitle());
        me.setExpCompany(formUser.getExpCompany());
        me.setExpStart(formUser.getExpStart());
        me.setExpEnd(formUser.getExpEnd());
        me.setExpDescription(formUser.getExpDescription());
        
        // Upload Foto (jika ada)
        if (!imageFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";

            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) uploadFolder.mkdirs();

            String fileName = me.getId() + "_" + imageFile.getOriginalFilename();
            File destination = new File(uploadDir + fileName);

            imageFile.transferTo(destination);

            me.setProfileImage(fileName);
        }

        userRepository.save(me);

        return "redirect:/profile";
    }
}
