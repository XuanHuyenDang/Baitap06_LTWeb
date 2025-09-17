package entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cate_id;

    private String cate_name;

    // Quan hệ nhiều Category -> 1 User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
