package wahdini.getajobcopy.service;

import wahdini.getajobcopy.model.Conversation;
import wahdini.getajobcopy.model.Message;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.ConversationRepository;
import wahdini.getajobcopy.repository.MessageRepository;
import wahdini.getajobcopy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Ambil semua conversation milik user dan isi field transient:
     * - otherUser (User)
     * - lastMessage (String)
     * 
     * Tidak menyertakan conversation yang user1 = user2
     */
    public List<Conversation> getAllConversations(Long myId) {

        List<Conversation> list =
                conversationRepository.findByUser1IdOrUser2Id(myId, myId)
                        .stream()
                        // Filter untuk menghindari self-chat
                        .filter(c -> !c.getUser1Id().equals(c.getUser2Id()))
                        .collect(Collectors.toList());

        for (Conversation c : list) {

            Long otherId = (c.getUser1Id().equals(myId))
                    ? c.getUser2Id()
                    : c.getUser1Id();

            User otherUser = userRepository.findById(otherId).orElse(null);
            c.setOtherUser(otherUser);

            // Ambil pesan terakhir sebagai Optional, lalu ektrak text-nya
            Optional<Message> lastMsgOpt = messageRepository
                    .findTopByConversationIdOrderBySentAtDesc(c.getId());

            c.setLastMessage(lastMsgOpt.map(Message::getMessageText).orElse(""));
        }

        return list;
    }

    // Ambil satu conversation by ID (tidak mengisi otherUser/lastMessage di sini)
    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId).orElse(null);
    }

    // Cek apakah conversation sudah ada, kalau tidak buat baru
    public Conversation getOrCreateConversation(Long user1Id, Long user2Id) {
        // Jangan buat conversation dengan diri sendiri
        if (user1Id.equals(user2Id)) {
            return null;
        }

        var existing = conversationRepository.findByUser1IdAndUser2Id(user1Id, user2Id);

        if (existing.isEmpty()) {
            existing = conversationRepository.findByUser2IdAndUser1Id(user1Id, user2Id);
        }

        if (existing.isPresent()) return existing.get();

        Conversation newConv = new Conversation(user1Id, user2Id);
        return conversationRepository.save(newConv);
    }
}
