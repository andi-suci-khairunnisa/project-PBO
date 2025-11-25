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

    // Getter
    public Long getId() { return id; }
    public Long getConversationId() { return conversationId; }
    public Long getSenderId() { return senderId; }
    public String getMessageText() { return messageText; }
    public java.sql.Timestamp getSentAt() { return sentAt; }

    // Setter
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public void setSentAt(java.sql.Timestamp sentAt) { this.sentAt = sentAt; }
}

// Model ini merepresentasikan pesan dalam percakapan antara dua pengguna.
// Field persisten:
// - id: identifier unik pesan
// - conversationId: ID percakapan yang berisi pesan ini
// - senderId: ID pengguna yang mengirim pesan
// - messageText: isi pesan
// - sentAt: waktu pesan dikirim (otomatis set ke sekarang saat construct)
// Transient alias: getContent() mengembalikan messageText untuk kompatibilitas
// dengan template HTML (template mengakses ${msg.content})
// Constructor: otomatis mengisi sentAt dengan timestamp sekarang
// Desain menerapkan SRP: model hanya menyimpan data pesan; logika percakapan
// dan notifikasi ditangani di service/controller layer.
