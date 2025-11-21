package wahdini.getajobcopy.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =============================
    // FIELD LOGIN DASAR
    // =============================
    private String username;
    private String password;

    // =============================
    // FIELD PROFIL TAMBAHAN
    // =============================
    private String fullName;        
    private String job;             
    private String phone;           
    private String location;        

    @Column(columnDefinition = "TEXT")
    private String description;     

    private Double rating;          
    private String profileImage;    

    // =============================
    // FIELD PENGALAMAN KERJA
    // =============================
    private String expTitle;          // Nama jabatan / posisi
    private String expCompany;        // Nama perusahaan / tempat kerja
    private String expStart;          // Tahun mulai atau tanggal mulai
    private String expEnd;            // Tahun selesai atau tanggal selesai
    
    @Column(columnDefinition = "TEXT")
    private String expDescription;    // Deskripsi pekerjaan

    // =============================
    // GETTER & SETTER
    // =============================

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

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

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
