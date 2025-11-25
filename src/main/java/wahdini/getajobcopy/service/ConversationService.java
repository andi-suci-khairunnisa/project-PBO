package wahdini.getajobcopy.service;

import wahdini.getajobcopy.model.Conversation;
import wahdini.getajobcopy.model.Message;
import wahdini.getajobcopy.model.User;
import wahdini.getajobcopy.repository.ConversationRepository;
import wahdini.getajobcopy.repository.MessageRepository;
import wahdini.getajobcopy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository, 
                               UserRepository userRepository, 
                               MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

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
                c.setLastMessageSenderId(lastMsgOpt.map(Message::getSenderId).orElse(null));
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

/*
 * ConversationService (Service Layer)
 * Tanggung Jawab: Mengelola logika bisnis conversation, termasuk enrichment data transient fields (otherUser, lastMessage, lastMessageSenderId).
 * SOLID Principles:
 *   - Single Responsibility: Service ini fokus hanya pada operasi conversation, pemisahan concern dari controller.
 *   - Dependency Inversion: Constructor injection untuk 3 repository, tidak terikat implementasi konkret.
 * Method tersedia:
 *   - getAllConversations(): Ambil semua conversation milik user + isi field transient (otherUser, lastMessage, lastMessageSenderId) untuk sidebar display.
 *   - getConversation(): Ambil satu conversation by ID (tanpa filling transient fields).
 *   - getOrCreateConversation(): Cek apakah conversation sudah ada; jika tidak, buat baru. Mencegah self-chat (user1 = user2).
 */
