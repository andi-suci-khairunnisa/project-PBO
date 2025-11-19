package wahdini.getajobcopy.repository;

import wahdini.getajobcopy.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Cari percakapan antar dua user (user1 → user2)
    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    // Cari percakapan antar dua user (user2 → user1)
    Optional<Conversation> findByUser2IdAndUser1Id(Long user1Id, Long user2Id);

    // Ambil semua percakapan milik user (tanpa urutan)
    List<Conversation> findByUser1IdOrUser2Id(Long userId1, Long userId2);

    // Ambil daftar percakapan user, urut percakapan terbaru → lama
    List<Conversation> findByUser1IdOrUser2IdOrderByIdDesc(Long userId1, Long userId2);
}
