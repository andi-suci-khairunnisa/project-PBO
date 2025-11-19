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
