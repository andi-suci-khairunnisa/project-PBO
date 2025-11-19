package wahdini.getajobcopy.service;

import wahdini.getajobcopy.model.Conversation;
import wahdini.getajobcopy.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    // Ambil semua conversation milik user
    public List<Conversation> getAllConversations(Long userId) {
        return conversationRepository.findByUser1IdOrUser2Id(userId, userId);
    }

    // Ambil satu conversation by ID
    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId).orElse(null);
    }

    // Cek apakah conversation sudah ada, kalau tidak buat
    public Conversation getOrCreateConversation(Long user1Id, Long user2Id) {

        var existing = conversationRepository.findByUser1IdAndUser2Id(user1Id, user2Id);

        if (existing.isEmpty()) {
            existing = conversationRepository.findByUser2IdAndUser1Id(user1Id, user2Id);
        }

        if (existing.isPresent()) return existing.get();

        Conversation newConv = new Conversation(user1Id, user2Id);
        return conversationRepository.save(newConv);
    }
}
