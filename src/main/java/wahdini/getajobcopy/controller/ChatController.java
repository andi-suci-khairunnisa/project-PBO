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

    private final UserRepository userRepository;
    private final ConversationService conversationService;
    private final MessageService messageService;

    public ChatController(UserRepository userRepository,
                          ConversationService conversationService,
                          MessageService messageService) {
        this.userRepository = userRepository;
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

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

    @GetMapping("/job/chat/{receiverId}")
    public String startChatFromJob(
            @PathVariable Long receiverId,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        User me = userRepository.findByUsername(username);

        // Dapatkan atau buat percakapan baru
        var conv = conversationService.getOrCreateConversation(me.getId(), receiverId);

        // Redirect langsung ke room chat yang benar
        return "redirect:/message/" + conv.getId();
    }
}

// Controller ini menangani alur chat/simple messaging:
// - Menampilkan daftar percakapan pemakai (`GET /message`)
// - Membuka percakapan tertentu dan menampilkan pesan (`GET /message/{id}`)
// - Mengirim pesan pada percakapan (`POST /message/send/{id}`)
// - Memulai percakapan dari halaman job dan melakukan redirect ke room chat
// Desain: controller hanya bertugas routing dan manajemen session; logika
// bisnis percakapan/pesan didelegasikan ke `ConversationService` dan
// `MessageService` untuk mematuhi Single Responsibility Principle.
