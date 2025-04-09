package Hospital.User.DTO;

import java.time.LocalDateTime;

import Hospital.User.Entity.User;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReadDTO {
	
	@Id
	private String UserId;
	
	private String UserPw;
	
	private String UserName;
	
	private String UserRegNum;
	
	private Integer UserGender;
	
	private String UserPhone;
	
	private String UserAddress1;
	
	private String UserAddress2;
	
	private LocalDateTime InsertDateTime;
	
	private byte[] User_Salt;
	
	public UserReadDTO FromUserInfo(User u) {
		this.UserId = u.getUserId();
		this.UserPw = u.getUserPw();
		this.UserName = u.getUserName();
		this.UserRegNum = u.getUserRegNum();
		this.UserGender = u.getUserGender();
		this.UserPhone = u.getUserPhone();
		this.UserAddress1 = u.getUserAddress1();
		this.UserAddress2 = u.getUserAddress2();
		this.InsertDateTime = u.getInsertDateTime();
		this.User_Salt = u.getUser_Salt();
		return this;
	}
	
	public static UserReadDTO UserInfoFactory(User u) {
		UserReadDTO uiDTO = new UserReadDTO();
		uiDTO.FromUserInfo(u);
		return uiDTO;
	}
}
