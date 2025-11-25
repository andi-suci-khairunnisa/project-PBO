package wahdini.getajobcopy.repository;

import wahdini.getajobcopy.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Ambil semua pesan dalam satu percakapan (urut lama → baru)
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    // Ambil pesan terakhir dalam satu percakapan (urut baru → lama)
    Optional<Message> findTopByConversationIdOrderBySentAtDesc(Long conversationId);
}

/*
 * MessageRepository (Interface Repository)
 * Tanggung Jawab: Menyediakan akses data untuk entity Message, memfasilitasi pengambilan dan pengelolaan pesan dalam conversation.
 * SOLID Principles:
 *   - Interface Segregation: Interface ini fokus hanya pada operasi Message, tidak ada method yang tidak perlu.
 *   - Dependency Inversion: Menggunakan abstraksi JpaRepository tanpa terikat implementasi.
 * Method tersedia:
 *   - findByConversationIdOrderBySentAtAsc(): Ambil semua pesan dalam satu conversation, diurutkan kronologis (lama → baru) untuk display UI
 *   - findTopByConversationIdOrderBySentAtDesc(): Ambil pesan terakhir (paling baru) untuk ditampilkan di conversation list preview
 */
