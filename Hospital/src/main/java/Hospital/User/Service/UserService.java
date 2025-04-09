package Hospital.User.Service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hospital.Appointment.Service.AppointmentService;
import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Entity.PatientRepository;
import Hospital.Patient.Service.PatientService;
import Hospital.User.DTO.UserCreateDTO;
import Hospital.User.DTO.UserReadDTO;
import Hospital.User.DTO.UserEditDTO;
import Hospital.User.Entity.User;
import Hospital.User.Entity.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PatientService ps;
	
	@Autowired
	private PatientRepository pr;

	@Autowired
	private AppointmentService aps;
	
	public List<User> findAll(){
		return this.userRepository.findAll();
	}
	
	
	public boolean UserLogin(String UserId, String UserPw) throws Exception{
		User userinfo = this.userRepository.findById(UserId).orElse(null);
		if(userinfo==null) {
			return false;
		}
		else if(!userinfo.getUserPw()
				.equals(PWSecurity.Hashing(UserPw.getBytes(), userinfo.getUser_Salt()))) {
			return false;
		}
		return true;
	}
	
	
    public void UserRegister(UserCreateDTO ucDTO) throws Exception {
        byte[] salt = PWSecurity.generateSalt(); 
        String hashedPassword = PWSecurity.Hashing(ucDTO.getUserPw().getBytes(), salt);
        User user = User.builder()
                .UserId(ucDTO.getUserId())
                .UserPw(hashedPassword)
                .UserName(ucDTO.getUserName())
                .UserRegNum(ucDTO.getUserRegNum())
                .UserGender(ucDTO.getUserGender())
                .UserPhone(ucDTO.getUserPhone())
                .UserAddress1(ucDTO.getUserAddress1())
                .UserAddress2(ucDTO.getUserAddress2())
                .User_Salt(salt) 
                .build();
        this.userRepository.save(user); 
    }
    
	public void ResetUserPw(String UserId, String UserPw) throws Exception {
		User user = this.userRepository.findById(UserId).orElseThrow();
		user.setUserPw(PWSecurity.Hashing(UserPw.getBytes(), user.getUser_Salt()));
		this.userRepository.save(user);
	}
	
	
 
	//회원 가입 아이디 중복 확인 버튼 로직
	public boolean UserIdCheck(String UserId) {
		return this.userRepository.existsById(UserId);
	}
	
	//회원정보 수정 로직
	public void UserModify(UserEditDTO uieDTO) throws Exception {
	    User user = this.userRepository.findById(uieDTO.getUserId()).orElseThrow();
	    // 비밀번호 인증 (입력한 비밀번호+회원가입시 부여받은 Salt vs DB에 저장된 비밀번호 + 회원가입 시 부여받은 Salt)
	    String inputPwHashed = PWSecurity.Hashing(uieDTO.getUserPw().getBytes(), user.getUser_Salt());
	    if (!inputPwHashed.equals(user.getUserPw())) {
	        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
	    }
	    // 정보 반영 및 저장
	    user = uieDTO.Fill(user);// 이름, 전화번호, 주소 등 업데이트
	    this.userRepository.save(user);
	}
	
	//회원정보 읽기 로직
	public UserReadDTO UserInfoRead(String UserId) {
		User userinfo = this.userRepository.findById(UserId).orElseThrow();
		return UserReadDTO.UserInfoFactory(userinfo);
	}
	
	//아이디 찾기 로직
	public UserReadDTO FindUserId(String UserName, String UserRegNum) {
		List<User> userinfolist = new ArrayList<User>();
		User userinforesult;
		userinfolist = this.userRepository.findAll();
		for(User user: userinfolist) {
			if((user.getUserName().equals(UserName))&&(user.getUserRegNum().equals(UserRegNum))) {
				userinforesult = user;
				return UserReadDTO.UserInfoFactory(userinforesult);
			}
		}	
		return  null;
	}
	
	//비번 찾기기능 로직
	public UserReadDTO FindUserPw(String UserId, String UserName, String UserRegNum) {
		
		User user = this.userRepository.findById(UserId).orElse(null);
		UserReadDTO urDTO = new UserReadDTO();
		if(user != null) {
			if((user.getUserName().equals(UserName)) && (user.getUserRegNum().equals(UserRegNum))) {
				return UserReadDTO.UserInfoFactory(user);
			}
			return urDTO = null;
		}
		else {	
			return urDTO = null;
		}
	}
	
	public boolean DeleteUserId(String UserId, String UserPw) throws Exception { 
		User userinfo = this.userRepository.findById(UserId).orElseThrow();
		List<Patient> allpatient = new ArrayList<Patient>();
		List<Patient> patient = new ArrayList<Patient>();
		allpatient = this.ps.findAll();
		System.out.println(allpatient);
		if((userinfo.getUserPw().equals(PWSecurity.Hashing(UserPw.getBytes(), userinfo.getUser_Salt())))) {
			for(Patient patients: allpatient) {
				if(UserId!=null && patients.getP_UserId().equals(UserId)){
					Integer p_id = patients.getP_Id();
					this.pr.deleteById(p_id);
				}
			}
			this.aps.AppointmentDeleteAllByUserId(UserId); 
			this.userRepository.delete(userinfo); 
			return true;
		}
		return false;
	}
}
