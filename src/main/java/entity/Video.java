package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "video_id")   
	private int videoId;
	
    @Column(nullable = false)
    private String title;

    private String description;

    private String url;      // đường dẫn video (có thể YouTube hoặc local)

    private String poster;   // ảnh thumbnail

    private int views = 0;   // mặc định 0 khi mới tạo

    private boolean active = true;  // mặc định video đang active
}