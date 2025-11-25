package wahdini.getajobcopy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.JobApplicationRepository;
import wahdini.getajobcopy.repository.UserRepository;
import wahdini.getajobcopy.service.ConversationService;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NotificationsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/api/notifications")
    public Map<String, Object> notifications(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            result.put("acceptedNotifications", 0);
            result.put("unreadMessages", 0);
            return result;
        }

        User me = userRepository.findByUsername(username);
        if (me == null) {
            result.put("acceptedNotifications", 0);
            result.put("unreadMessages", 0);
            return result;
        }

        int acceptedCount = jobApplicationRepository.findByUserAndStatus(me, "ACCEPTED").size();

        int unreadMessages = 0;
        List<wahdini.getajobcopy.model.Conversation> convs = conversationService.getAllConversations(me.getId());
        for (var c : convs) {
            Long senderId = c.getLastMessageSenderId();
            if (senderId != null && !senderId.equals(me.getId())) {
                unreadMessages++;
            }
        }

        result.put("acceptedNotifications", acceptedCount);
        result.put("unreadMessages", unreadMessages);
        return result;
    }
}
