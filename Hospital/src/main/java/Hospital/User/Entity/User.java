package Hospital.User.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@Column(length = 12, columnDefinition = "VARCHAR(12) COLLATE utf8mb4_bin")
	private String UserId;
	@Column(columnDefinition = "CHAR(64)")
	private String UserPw;
	@Column(columnDefinition = "VARCHAR(4)")
	private String UserName;
	@Column(columnDefinition = "CHAR(6)")
	private String UserRegNum;
	@Column(columnDefinition = "TINYINT")
	private Integer UserGender;
	@Column(columnDefinition = "CHAR(11) COLLATE ascii_bin")
	private String UserPhone;
	@Column(columnDefinition = "VARCHAR(50)")
	private String UserAddress1;
	@Column(columnDefinition = "VARCHAR(25)")
	private String UserAddress2;
	@Column(columnDefinition = "BINARY(16)", nullable = false)
	private byte[] User_Salt; 
	@CreationTimestamp
	private LocalDateTime InsertDateTime;
}
