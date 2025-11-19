package wahdini.getajobcopy.model;

import jakarta.persistence.*;
import jakarta.persistence.Transient;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    // ============== FIELD TAMBAHAN (TIDAK DISIMPAN DB) ==============
    @Transient
    private User otherUser;

    @Transient
    private String lastMessage;

    public Conversation() {}

    public Conversation(Long user1Id, Long user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    // Getter & Setter
    public Long getId() { return id; }

    public Long getUser1Id() { return user1Id; }
    public void setUser1Id(Long user1Id) { this.user1Id = user1Id; }

    public Long getUser2Id() { return user2Id; }
    public void setUser2Id(Long user2Id) { this.user2Id = user2Id; }

    public User getOtherUser() { return otherUser; }
    public void setOtherUser(User otherUser) { this.otherUser = otherUser; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
}
