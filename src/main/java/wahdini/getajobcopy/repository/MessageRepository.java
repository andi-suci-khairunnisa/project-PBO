package wahdini.getajobcopy.repository;

import wahdini.getajobcopy.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Ambil pesan berdasarkan conversation_id
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);
}
