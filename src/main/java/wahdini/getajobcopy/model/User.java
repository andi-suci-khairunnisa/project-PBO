package wahdini.getajobcopy.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Field Login Dasar
    private String username;
    private String password;

    // Field Profil Tambahan
    private String fullName;        
    private String job;             
    private String phone;           
    private String location;        

    @Column(columnDefinition = "TEXT")
    private String description;     

    private String profileImage;    

    // Field Pengalaman Kerja
    private String expTitle;
    private String expCompany;
    private String expStart;
    private String expEnd;
    
    @Column(columnDefinition = "TEXT")
    private String expDescription;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getExpTitle() { return expTitle; }
    public void setExpTitle(String expTitle) { this.expTitle = expTitle; }

    public String getExpCompany() { return expCompany; }
    public void setExpCompany(String expCompany) { this.expCompany = expCompany; }

    public String getExpStart() { return expStart; }
    public void setExpStart(String expStart) { this.expStart = expStart; }

    public String getExpEnd() { return expEnd; }
    public void setExpEnd(String expEnd) { this.expEnd = expEnd; }

    public String getExpDescription() { return expDescription; }
    public void setExpDescription(String expDescription) { this.expDescription = expDescription; }
}

/*
 * User (Entity Model)
 * Tanggung Jawab: Merepresentasikan data user dalam aplikasi, termasuk kredensial login dan profil lengkap.
 * SOLID Principles:
 *   - Single Responsibility: Model ini fokus hanya pada state/data user, tidak ada business logic kompleks.
 *   - Open/Closed: Entity dapat diperluas dengan field baru tanpa memodifikasi logic yang ada.
 * 
 * Field yang disediakan dibagi menjadi 3 kategori:
 *   - Login: username, password (untuk autentikasi).
 *   - Profil: fullName, job, phone, location, description, profileImage (info personal user).
 *   - Pengalaman Kerja: expTitle, expCompany, expStart, expEnd, expDescription (riwayat kerja user).
 * 
 * User entity dipetakan ke tabel "user" di database dan dapat dihubungkan ke entity lain 
 * (Job sebagai creator, JobApplication sebagai applicant, Message sebagai sender/receiver, dll).
 */
