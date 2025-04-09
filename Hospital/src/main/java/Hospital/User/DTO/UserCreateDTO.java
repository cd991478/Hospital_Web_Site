package Hospital.User.DTO;

import java.security.SecureRandom;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
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
	@NonNull
	private byte[] Salt;
}
