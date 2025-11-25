package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.UserRepository;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    

    @ModelAttribute
    public void addNotifications(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return; // not logged in

        User me = userRepository.findByUsername(username);
        if (me == null) return;

        // count accepted job applications for this user (applicant perspective)
        int acceptedCount = jobApplicationRepository.findByUserAndStatus(me, "ACCEPTED").size();
        model.addAttribute("acceptedNotifications", acceptedCount);
        
    }
}
