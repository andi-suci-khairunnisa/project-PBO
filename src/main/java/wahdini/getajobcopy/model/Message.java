package wahdini.getajobcopy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "sent_at")
    private java.sql.Timestamp sentAt;

    // Alias supaya HTML bisa pakai msg.content
    @Transient
    public String getContent() {
        return messageText;
    }

    public Message() {}

    public Message(Long conversationId, Long senderId, String messageText) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.sentAt = new java.sql.Timestamp(System.currentTimeMillis());
    }

    // Getter & Setter
    public Long getId() { return id; }

    public Long getConversationId() { return conversationId; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public java.sql.Timestamp getSentAt() { return sentAt; }
    public void setSentAt(java.sql.Timestamp sentAt) { this.sentAt = sentAt; }
}
