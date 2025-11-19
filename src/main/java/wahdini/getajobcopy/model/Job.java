package wahdini.getajobcopy.model;

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

    // === RELASI KE USER PEMBUAT JOB ===
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Job() {}

    public Job(String title, String location, String price, String description, User user) {
        this.title = title;
        this.location = location;
        this.price = price;
        this.description = description;
        this.user = user;
    }

    // GETTER
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    // SETTER
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
