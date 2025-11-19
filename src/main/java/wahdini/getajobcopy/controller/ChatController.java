package wahdini.getajobcopy.controller;

import jakarta.servlet.http.HttpSession;
import wahdini.getajobcopy.model.Conversation;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.UserRepository;
import wahdini.getajobcopy.service.ConversationService;
import wahdini.getajobcopy.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageService messageService;

    // ===========================
    // LIST CONVERSATION
    // ===========================
    @GetMapping("/message")
    public String listMessages(HttpSession session, Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User me = userRepository.findByUsername(username);

        var conversations = conversationService.getAllConversations(me.getId());
        model.addAttribute("conversations", conversations);
        model.addAttribute("selectedConversation", null);

        return "message";
    }

    // ===========================
    // OPEN ONE CONVERSATION
    // ===========================
    @GetMapping("/message/{conversationId}")
    public String openConversation(
            @PathVariable Long conversationId,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User me = userRepository.findByUsername(username);

        // sidebar list
        model.addAttribute("conversations",
                conversationService.getAllConversations(me.getId()));

        // conversation
        Conversation conv = conversationService.getConversation(conversationId);
        model.addAttribute("selectedConversation", conv);

        // messages
        model.addAttribute("messages",
                messageService.getMessages(conversationId));

        return "message";
    }

    // ===========================
    // SEND MESSAGE
    // ===========================
    @PostMapping("/message/send/{conversationId}")
    public String sendMessage(
            @PathVariable Long conversationId,
            @RequestParam String content,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        User me = userRepository.findByUsername(username);

        messageService.sendMessage(conversationId, me.getId(), content);

        return "redirect:/message/" + conversationId;
    }
}
