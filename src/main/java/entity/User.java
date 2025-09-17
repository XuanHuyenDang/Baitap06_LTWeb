package entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    private String username;
    private String password;
    private int role_id;

    // 👉 Thêm các field còn thiếu
    private String fullname;
    private String phone;
    private String image;

    // Quan hệ 1 User -> nhiều Category
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Category> categories;
}
