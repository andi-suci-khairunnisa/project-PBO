package wahdini.getajobcopy.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String price;
    private String description;
    private String duration;
    private String kategori;
    private String phone;

    private LocalDateTime postedDate;
    private LocalDateTime lastViewedAt;
    private String status;

    // === RELASI KE USER PEMBUAT JOB ===
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Job() {}

    // Getter
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getDuration() { return duration; }
    public String getKategori() { return kategori; }
    public String getPhone() { return phone; }
    public LocalDateTime getPostedDate() { return postedDate; }
    public LocalDateTime getLastViewedAt() { return lastViewedAt; }
    public String getStatus() { return status; }
    public User getUser() { return user; }

    // Setter
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setLocation(String location) { this.location = location; }
    public void setPrice(String price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate; }
    public void setLastViewedAt(LocalDateTime lastViewedAt) { this.lastViewedAt = lastViewedAt; }
    public void setStatus(String status) { this.status = status; }
    public void setUser(User user) { this.user = user; }

    @PrePersist
    protected void onCreate() {
        this.postedDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }
}

// Model ini merepresentasikan entitas pekerjaan/job yang diposting oleh pemilik.
// Field persisten:
// - Informasi dasar: title, location, price, description, duration, kategori, phone
// - Metadata: postedDate (otomatis set saat insert), status (ACTIVE/ASSIGNED/FINISHED),
//   lastViewedAt (diupdate saat pemilik buka halaman pelamar untuk tracking notifikasi)
// - Relasi: user (pemilik yang memposting job)
// Lifecycle: @PrePersist mengisi postedDate dan status default otomatis.
// Desain menerapkan SRP: model hanya menyimpan data dan lifecycle hook, logika
// bisnis (notifikasi, status transition) ada di controller/service layer.
