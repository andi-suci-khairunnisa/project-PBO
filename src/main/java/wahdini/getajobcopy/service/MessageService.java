package wahdini.getajobcopy.service;

import wahdini.getajobcopy.model.Message;
import wahdini.getajobcopy.repository.MessageRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Long conversationId, Long senderId, String text) {
        Message msg = new Message(conversationId, senderId, text);
        return messageRepository.save(msg);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}

/*
 * MessageService (Service Layer)
 * Tanggung Jawab: Mengelola logika bisnis message, termasuk pengiriman dan pengambilan pesan dalam conversation.
 * SOLID Principles:
 *   - Single Responsibility: Service ini fokus hanya pada operasi message, pemisahan concern dari controller.
 *   - Dependency Inversion: Constructor injection untuk MessageRepository, tidak terikat implementasi konkret.
 * Method tersedia:
 *   - sendMessage(): Buat Message baru dan simpan ke database (constructor Message otomatis set sentAt timestamp).
 *   - getMessages(): Ambil semua pesan dalam conversation, diurutkan kronologis (lama → baru) untuk display UI.
 */
