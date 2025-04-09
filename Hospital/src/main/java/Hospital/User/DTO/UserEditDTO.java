package Hospital.User.DTO;

import Hospital.User.Entity.User;
import Hospital.User.Service.PWSecurity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserEditDTO {
	
	@NonNull
	private String UserId;
	@NonNull
	private String UserPw;
	@NonNull
	private String UserName;
	@NonNull
	private String UserRegNum;
	@NonNull
	private Integer UserGender;
	@NonNull
	private String UserPhone;
	@NonNull
	private String UserAddress1;
	@NonNull
	private String UserAddress2;

	 // DTO 데이터를 UserInfo 엔티티에 반영
    public User Fill(User u) throws Exception {
        u.setUserPw(PWSecurity.Hashing(UserPw.getBytes(), u.getUser_Salt())); //수정된 해싱 방식 적용
        u.setUserName(this.UserName);
        u.setUserRegNum(this.UserRegNum);
        u.setUserGender(this.UserGender);
        u.setUserPhone(this.UserPhone);
        u.setUserAddress1(this.UserAddress1);
        u.setUserAddress2(this.UserAddress2);
        return u;
    }
}
