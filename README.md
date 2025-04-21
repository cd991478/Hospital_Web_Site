### - 프로젝트명
- 대구 의료 서비스 웹 사이트

### - 개발 환경
- Java, Spring Boot STS 4, MySQL DB, MySQL WorkBench 8.0 CE, HTML 5, JavaScript, Thymeleaf, D3.js 등

### - 주요 기능
- 회원 가입, 회원 탈퇴, 로그인, 회원 정보 수정, 아이디 비밀번호 찾기 기능
- Covid-19 문진표 등록, 조회, 수정, 삭제 기능 (회원 전용)
- 병원 예약 및 취소기능 (회원 전용)
- 지역, 진료과목별 병원 검색 및 전체 조회 기능
- 등록된 전체 Covid-19 환자의 성별, 연령대별 데이터 차트 조회 기능
- 등록된 대구 병원의 지역별, 진료과목별 차트 조회 기능
- 관리자 전용 기능
- 공지사항 게시판 기능 (관리자의 게시글 등록, 수정, 삭제 기능)

### - 구동 과정

---

#### 1. 웹 사이트 홈
![1](./images/1.png)
```java
// build.gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.4.3'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'Hospital'
version = '0.0.1-SNAPSHOT'

java {
	sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
gradle.rootProject {
    ext {
        javaHome = "C:/Program Files/Java/jdk-17"
    }
}
configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-logging'  // 로깅
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.mysql:mysql-connector-j'
	runtimeOnly 'org.webjars:bootstrap:4.5.0'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.2'
   	 testImplementation 'org.junit.jupiter:junit-jupiter-engine:5.8.2'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0'
	implementation 'org.json:json:20210307'
}
bootJar {
    mainClass = 'Hospital.HospitalApplication'
}
```

```java
// application.properties
spring.application.name=Hospital
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test_db
spring.datasource.username=admin
spring.datasource.password=0000
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.thymeleaf.cache=false

spring.jackson.serialization.FAIL_ON_EMPTY_BEANS=false
spring.cache.type=simple
logging.level.org.springframework.cache=DEBUG
```

```java
@GetMapping("/Home") // 홈 화면 이동
	public String GotoHome(HttpSession session, Model model) {
		String UserId = (String)session.getAttribute("UserId");
		if(UserId != null) {
			UserReadDTO uiDTO = this.userService.UserInfoRead(UserId);
			model.addAttribute("UserId",UserId);
			model.addAttribute("UserName",uiDTO.getUserName());
			session.setAttribute("UserId",UserId);
			session.setAttribute("UserName",uiDTO.getUserName());
		}
		else {
			model.addAttribute("UserId",null);
			model.addAttribute("UserName",null);
		}
		   //  의료 속보 데이터 추가
        List<SpotNewsResponseDTO> spotNewsList = sns.getAllNews();
        model.addAttribute("spotNewsList", spotNewsList);
        model.addAttribute("recentPosts", boardService.RecentBoardList());
        return "/Home";
	}
```

```html
<div th:fragment="navbar">
  <nav class="navbar navbar-expand-lg bg-light" style="font-weight: bold;">
    <a th:href="@{/Home}">
      <img src="/images/병원로고.jpg" style="height:40px;width:40px;margin-left:10px;">
    </a>
    <a class="navbar-brand" th:href="@{/Home}" style="margin-left:20px; margin-right:150px; max-width:300px; ">
      <span style="font-size:25px;">대구 통합 의료 서비스　　　</span>
    </a>

    <div class="container text-center" style="margin-right:20%;">
      <ul class="navbar-nav ml-auto">
        <li class="nav-item dropdown">
          <a href="#" class="nav-link">COVID-19 문진표</a>
        <div class="dropdown-menu" th:if="${UserId == null}">
            <a href="#" onclick="CheckLogin()" class="dropdown-item">COVID-19 문진표작성</a>
            <a href="#" onclick="CheckLogin()" class="dropdown-item">COVID-19 문진표조회</a>
          </div>
          <div class="dropdown-menu" th:if="${UserId != null}">
            <a th:href="@{/PatientInfo/Input}" class="dropdown-item">COVID-19 문진표작성</a>
            <a th:href="@{/PatientInfo/List}" class="dropdown-item">COVID-19 문진표조회</a>
          </div>
        </li>
        <li class="nav-item dropdown">
          <a href="#" class="nav-link">예약</a>
          <div class="dropdown-menu" th:if="${UserId == null}">
            <a href="#" onclick="CheckLogin()" class="dropdown-item">병원예약</a>
            <a href="#" onclick="CheckLogin()" class="dropdown-item">예약조회</a>
          </div>
          <div class="dropdown-menu" th:if="${UserId != null}">
            <a th:href="@{/AppointmentPage/Input}" class="dropdown-item">병원예약</a>
            <a th:href="@{/AppointmentPage/List}" class="dropdown-item">예약조회</a>
          </div>
        </li>
        <li class="nav-item dropdown">
          <a href="#" class="nav-link">정보</a>
          <div class="dropdown-menu">
            
            <a th:href="@{/AllHospitalList}" class="dropdown-item">전체병원조회</a>
            <a th:href="@{/BoardPage/List}" class="dropdown-item">공지사항</a>
            <a th:href="@{/BoardPage/FAQ}" class="dropdown-item">자주묻는질문</a>
          </div>
        </li>
		<li class="nav-item dropdown">
			<a href="#" class="nav-link">통계자료</a>
			<div class="dropdown-menu">
				<a th:href="@{/Chart/ShowPatientRegion}" class="dropdown-item">COVID-19 감염자 분포</a>
					<a th:href="@{/Chart/ShowPatient}" class="dropdown-item">COVID-19 문진표 통계</a>
					<a th:href="@{/Chart/DetailPatient}" class="dropdown-item">COVID-19 문진표 세부 통계</a>
					<a th:href="@{/Chart/ShowHospitalRegion}" class="dropdown-item">대구 병원 지역별 통계</a>
			</div>
		</li>
        <li class="nav-item dropdown" th:if="${UserId == null}">
          <a th:href="@{/UserPage/Login}" class="nav-link">로그인</a>
          <div class="dropdown-menu">
            <a th:href="@{/UserPage/Login}" class="dropdown-item">로그인</a>
            <a th:href="@{/UserPage/SignUp}" class="dropdown-item">회원가입</a>
            <a th:href="@{/UserPage/FindUserId}" class="dropdown-item">아이디 찾기</a>
            <a th:href="@{/UserPage/FindUserPw}" class="dropdown-item">비밀번호 찾기</a>
          </div>
        </li>
        <li class="nav-item dropdown" th:if="${(UserId != null) and (UserId != 'admin')}">
          <a href="#" class="nav-link">회원메뉴</a>
       <div class="dropdown-menu">
              <a th:href="@{/UserPage/EditUserInfo}" class="dropdown-item">마이페이지</a>
              <a th:href="@{/UserPage/Logout}" class="dropdown-item">로그아웃</a>
       </div>
      </li>

        <li class="nav-item dropdown" th:if="${(UserId != null) and (UserId == 'admin')}">
          <a th:href="@{/AdminPage/Menu}" class="nav-link">관리자메뉴</a>
          <div class="dropdown-menu">
            <a th:if="${UserId == 'admin'}" th:href="@{/AdminPage/Menu}" class="dropdown-item">관리자메뉴</a>
            <a th:href="@{/UserPage/EditUserInfo}" class="dropdown-item">관리자정보</a>
            <a th:href="@{/UserPage/Logout}" class="dropdown-item">로그아웃</a>
          </div>
        </li>
        
      </ul>
    </div>
    <div th:if="${UserId == null}" style="width:300px;" th:text="|로그인 해주세요|">　</div>
    <div th:if="${UserId != null}" style="width:300px;margin-right:50px;margin-left:50px;" 
		th:text="'환영합니다,　' + ${UserName} + ' (' + ${UserId} + ') 님'"></div>
  </nav>
```

- 홈 화면의 상단에 주요 메뉴들이 있으며, 메뉴에 마우스를 올리면 하위 메뉴들이 열리고, 클릭하면 해당 세부 메뉴로 이동됩니다.

---

#### 2. 회원가입 및 로그인
![2c](./images/2c.png)

```java
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
```

```java
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
```

```java
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// 로그인 수행
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

	// 회원가입 수행
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
    
    //회원 가입시 아이디 중복 확인
  	public boolean UserIdCheck(String UserId) {
  		return this.userRepository.existsById(UserId);
  	}
}
```

```java
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/UserPage/Login") // 로그인 화면으로 이동
		public String GotoLogin() {
			return "/UserPage/Login";
		}

	@PostMapping("/UserPage/Login") // 로그인 버튼 누르면 실행
	public String Login(@RequestParam("UserId") String UserId,
						@RequestParam("UserPw") String UserPw,
						HttpSession session, Model model) throws Exception  {
		boolean Result = this.userService.UserLogin(UserId,UserPw);
		if(Result == true){
			session.setAttribute("UserId", UserId);
			System.out.println(UserId);
			return "redirect:/Home";
		}
		else {
			model.addAttribute("Error", "아이디 또는 비밀번호가 틀렸습니다.");
			return "/UserPage/Login";
		}
	}
	
	@GetMapping("/UserPage/SignUp") // 회원가입 버튼을 누르면 회원가입 화면으로 이동 
	public String GotoSignUp() {
		return "/UserPage/SignUp";
	}
	
	@PostMapping("/UserPage/SignUp") // 회원가입 화면에서, 입력 후 회원가입 완료 누르면 실행 후 이동
	public String SignUp(UserCreateDTO ucDTO, RedirectAttributes rda) throws Exception {
		if(this.userService.UserIdCheck(ucDTO.getUserId())) {
			rda.addFlashAttribute("Error","사용할 수 없는 아이디입니다.");
			rda.addFlashAttribute("UserId",ucDTO.getUserId());
			rda.addFlashAttribute("UserName",ucDTO.getUserName());
			rda.addFlashAttribute("UserRegNum",ucDTO.getUserRegNum());
			rda.addFlashAttribute("UserPhone",ucDTO.getUserPhone());
			rda.addFlashAttribute("UserGender",ucDTO.getUserGender());
			rda.addFlashAttribute("UserAddress1",ucDTO.getUserAddress1());
			rda.addFlashAttribute("UserAddress2",ucDTO.getUserAddress2());
			return "redirect:/UserPage/SignUp";
		}
		this.userService.UserRegister(ucDTO);
		return "/UserPage/SignUpComplete";
	}
	
	
	@PostMapping("/UserPage/CheckUserId") 
	@ResponseBody// 아이디 중복체크
	public Map<String, String> CheckUserId(@RequestBody Map<String, String> requestData) {
	    String userId = requestData.get("userId");
	    Map<String, String> response = new HashMap<>();
	    if (this.userService.UserIdCheck(userId)) {
	        response.put("status", "error");
	        response.put("message", "아이디가 이미 존재합니다.");
	    } else {
	        response.put("status", "success");
	        response.put("message", "사용 가능한 아이디입니다.");
	    }	    
	    return response;
	}
	
	
	@GetMapping("/UserPage/Logout") // 로그아웃
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/Home";
	}
}
```

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>회원가입</title>
  <link rel="stylesheet" href="/webjars/bootstrap/4.5.0/css/bootstrap.min.css" />
  <script>
    function CheckUserId() {
        var userId = document.getElementById('UserId').value;

        // 아이디 형식 검사
        var idPattern = /^(?=[a-zA-Z0-9]{4,12}$)(?![0-9]{4,12}$)[a-zA-Z0-9]+$/;
        if (!idPattern.test(userId)) {
            document.getElementById('IdError').style.color = "red";
            document.getElementById('IdError').textContent = "사용할 수 없는 아이디입니다.";
            return; // 형식이 잘못되면 중복 확인을 진행하지 않음
        }

        // Ajax 요청
        fetch('/UserPage/CheckUserId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId: userId })
        })
        .then(response => response.json())
        .then(data => {
            var errorElement = document.getElementById('IdError');
            if (data.status === 'error') {
                errorElement.style.color = "red";
                errorElement.textContent = "아이디가 이미 존재합니다.";
            } else {
                errorElement.style.color = "green";
                errorElement.textContent = "사용 가능한 아이디입니다.";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('IdError').textContent = "서버 오류가 발생했습니다.";
        });
    }

    function CheckForm(event) {
        var id = document.getElementsByName("UserId")[0].value;
        var pw = document.getElementsByName("UserPw")[0].value;
        var pwc = document.getElementsByName("UserPwc")[0].value; 
        var name = document.getElementsByName("UserName")[0].value;
        var regnum = document.getElementsByName("UserRegNum")[0].value;
        var phone = document.getElementsByName("UserPhone")[0].value;

        var idPattern = /^(?=[a-zA-Z0-9]{4,12}$)(?![0-9]{4,12}$)[a-zA-Z0-9]+$/;
        var pwPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/;
        var namePattern = /^[가-힣]{2,4}$/; //2-4자리 이름 허용
        var phonePattern = /^\d{11}$/; // 11자리 숫자만 허용
        var regnumPattern = /^\d{6}$/; // 6자리 숫자만 허용

       
        if (!idPattern.test(id)) { // 아이디 검증
            alert("아이디는 4~12자 사이의 영어와 숫자 조합만 가능합니다. 숫자만 포함된 아이디는 사용할 수 없습니다.");
            event.preventDefault();
            return false;
        }     
        if (!pwPattern.test(pw)) {
            alert("비밀번호는 8~20자리이며, 영문자/숫자를 포함해야 합니다.");// 비밀번호 검증
            event.preventDefault();
            return false;
        }      
        if (pw !== pwc) {
            alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");// 비밀번호 확인
            event.preventDefault();
            return false;
        }
        if (!namePattern.test(name)) {
            alert("이름은 2~4글자 사이의 한글만 입력 가능합니다.");// 이름 검증
            event.preventDefault();
            return false;
        }
        if (!regnumPattern.test(regnum)) {
            alert("주민번호는 앞 6자리 숫자만 입력해주세요.");// 주민번호 검증
            event.preventDefault();
            return false;
        }
        if (!phonePattern.test(phone)) {
            alert("전화 번호는 숫자 11자리로만 입력해주세요.\n예) 01012341234");// 전화번호 검증
            event.preventDefault();
            return false;
        }
        return true;
    }
  </script>
</head>
<body>
<div th:replace="fragments/navbar :: navbar"></div>
<header th:insert="/UserPage/Header.html"></header>
  <div class="container mt-5" style="max-width: 400px;">
    <h3 class="text-center">회원가입</h3>
    <hr>

    <form method="POST" id="SignUpForm" onsubmit="return CheckForm(event)">
      <div class="form-group">
        <label for="UserId">* 아이디</label>
        <div class="input-group">
          <input type="text" name="UserId" id="UserId" class="form-control" placeholder="4~12자리 영어/숫자" required/>
          <div class="input-group-append">
            <button type="button" class="btn btn-secondary" id="CheckIdButton" onclick="CheckUserId()">중복확인</button>
          </div>
        </div>
        <small id="IdError" class="form-text"></small>
      </div>

      <div class="form-group">
        <label for="UserPw">* 비밀번호</label>
        <input type="password" name="UserPw" class="form-control" placeholder="8~20자리 영어+숫자" required/>
      </div>

      <div class="form-group">
        <label for="UserPwc">* 비밀번호 확인</label>
        <input type="password" name="UserPwc" class="form-control" placeholder="비밀번호 다시 입력" required/>
      </div>
	<hr>
      <div class="form-group">
        <label for="UserName">* 이름</label>
        <input type="text" name="UserName" class="form-control" placeholder="예) 홍길동" required/>
      </div>

      <div class="form-group">
        <label for="UserRegNum">* 주민등록번호 앞자리</label>
        <input type="text" name="UserRegNum" class="form-control" placeholder="예) 921014" required/>
      </div>

      <div class="form-group">
        <label>* 성별</label><br>
        <div class="form-check form-check-inline">
          <input type="radio" name="UserGender" value="1" class="form-check-input" required/>
          <label class="form-check-label">여성</label>
        </div>
        <div class="form-check form-check-inline">
          <input type="radio" name="UserGender" value="0" class="form-check-input"/>
          <label class="form-check-label">남성</label>
        </div>
      </div>

      <div class="form-group">
        <label for="UserPhone">* 휴대폰 번호</label>
        <input type="text" name="UserPhone" class="form-control" placeholder="예) 01012341234" required/>
      </div>

	  <div class="form-group">
	      <label for="UserAddress1">* 주소</label>
	      <div class="input-group">
	          <input type="text" name="UserAddress1" id="UserAddress1" class="form-control" placeholder="주소 검색 버튼을 눌러주세요" required readonly/>
	          <div class="input-group-append">
	              <button type="button" class="btn btn-secondary" onclick="execDaumPostcode()">주소 검색</button>
	          </div>
	      </div>
	  </div>

      <div class="form-group">
        <label for="UserAddress2">* 상세 주소</label>
        <input type="text" name="UserAddress2" class="form-control" placeholder="예) xx빌라 101호" required/>
      </div>

      <button type="submit" class="btn btn-primary btn-block">회원가입</button>
    </form>

    <div th:if="${Error}" class="mt-3 text-danger">
      <p th:text="${Error}"></p>
    </div>

    <div class="mt-3 text-center">
      <a class="btn btn-link" th:href="@{/UserPage/Login}" role="button">취소</a>
      <a class="btn btn-link" th:href="@{/Home}" role="button">홈으로</a>
    </div>
  </div>
  <br><br><br>
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> // 주소 검색 API
  <script>
      function execDaumPostcode() {
          new daum.Postcode({
              oncomplete: function(data) {
                  var fullAddr = ''; 
                  var extraAddr = '';

                  if (data.userSelectedType === 'R') {
                      fullAddr = data.roadAddress;
                  } else {
                      fullAddr = data.jibunAddress;
                  }

                  if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                      extraAddr += data.bname;
                  }

                  if (data.buildingName !== '' && data.apartment === 'Y') {
                      extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                  }

                  if (extraAddr !== '') {
                      fullAddr += ' (' + extraAddr + ')';
                  }

                  document.getElementById('UserAddress1').value = fullAddr;
              }
          }).open();
      }
  </script>

  <script src="/webjars/jquery/3.5.1/jquery.min.js"></script>
  <script src="/webjars/bootstrap/4.5.0/js/bootstrap.min.js"></script>
</body>
</html>

```

- 주요 기능인 문진표 등록, 병원 예약을 이용하기위해선 회원 가입을 하여 로그인 하여야 합니다.


---

#### 3. 아이디 찾기 및 비밀번호 찾기

![2-z](./images/2-z.png)

```java
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
```


```java
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	//회원정보 읽기
  	public UserReadDTO UserInfoRead(String UserId) { 
  		User userinfo = this.userRepository.findById(UserId).orElseThrow();
  		return UserReadDTO.UserInfoFactory(userinfo);
  	}

	//아이디 찾기
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
	
	//비번 찾기
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

	//비밀번호 재설정
	public void ResetUserPw(String UserId, String UserPw) throws Exception {
		User user = this.userRepository.findById(UserId).orElseThrow();
		user.setUserPw(PWSecurity.Hashing(UserPw.getBytes(), user.getUser_Salt()));
		this.userRepository.save(user);
	}
}
```


```java
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/UserPage/FindUserId") // 로그인 페이지에서 아이디 찾기 누르면 이동
	public String GotoFindUserId() {
		return "/UserPage/FindUserId";
	}
	
	@PostMapping("/UserPage/FindUserId") // 아이디 찾기 페이지에서 정보 입력 후 찾기 버튼 누르면 수행
	public ModelAndView FindUserId(@RequestParam("UserName") String UserName,
				       @RequestParam("UserRegNum") String UserRegNum,
								     Model model) {
		ModelAndView mav = new ModelAndView();
		UserReadDTO uiDTO = new UserReadDTO();
		uiDTO = this.userService.FindUserId(UserName, UserRegNum);
		if(uiDTO != null) {
			mav.addObject("FindUserInfoResult", uiDTO);
			mav.setViewName("/UserPage/FindUserIdResult");
			return mav;
		}
		else {
			model.addAttribute("error","입력하신 정보와 일치하는 회원정보가 없습니다.");
			mav.setViewName("/UserPage/FindUserId");
			return mav;
		}
	}
	
	@GetMapping("/UserPage/FindUserIdResult") // 아이디 찾기 성공시 결과 페이지 이동
	public String GotoFindUserIdResult() {
		return "/UserPage/FindUserIdResult";
	}
	
	@GetMapping("/UserPage/FindUserPw") // 로그인 페이지에서 비밀번호 찾기 누르면 이동
	public String GotoFindUserPw() {
		return "/UserPage/FindUserPw";
	}
	
	@PostMapping("/UserPage/FindUserPw") // 비밀번호 찾기 페이지에서 정보 입력 후 버튼 누르면 수행
	public ModelAndView FindUserPw(@RequestParam("UserId") String UserId,
				       @RequestParam("UserName") String UserName,
				       @RequestParam("UserRegNum") String UserRegNum,
								     Model model) {
		ModelAndView mav = new ModelAndView();
		UserReadDTO uiDTO = new UserReadDTO();
		uiDTO = this.userService.FindUserPw(UserId, UserName, UserRegNum);
		if(uiDTO != null) {
			mav.addObject("FindUserInfoResult",uiDTO);
			mav.setViewName("/UserPage/ResetUserPw");
			return mav;
		}
		else {
			model.addAttribute("error","입력하신 정보와 일치하는 회원정보가 없습니다.");
			mav.setViewName("/UserPage/FindUserPw");
			return mav;
		}
	}

	@PostMapping("/UserPage/ResetUserPw") // 비밀번호 재설정 버튼 누르면 수행
	public String ResetUserPw(@RequestParam("UserId") String UserId,
				  @RequestParam("UserPw") String UserPw) throws Exception {
		
		this.userService.ResetUserPw(UserId, UserPw);
		return "/UserPage/ResetUserPwResult";
	}
}
```

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 찾기 - 회원 정보 조회 결과</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css">
   <style>
  body {
  	    font-family: 'Pretendard', sans-serif;
  	}
   </style>
  <script>
    function CheckForm(event) {
      var pw = document.getElementsByName("UserPw")[0].value;
      var pwc = document.getElementsByName("UserPwc")[0].value;
      var pwPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,20}$/;

      if (pw !== pwc) {
        alert("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        event.preventDefault();
        return false;
      }

      if (!pwPattern.test(pw)) {
        alert("비밀번호는 8~20자리이며, 영문자와 숫자를 포함해야 합니다.");
        event.preventDefault();
        return false;
      }
      return true;
    }
  </script>
</head>
<body>
<div th:replace="fragments/navbar :: navbar"></div>
<header th:insert="/UserPage/Header.html"></header>
  <div class="container mt-5" style="max-width: 450px;">
    <h3 class="mb-4">비밀번호 찾기 - 회원 정보 조회 결과</h3>

    <div th:if="${FindUserInfoResult.UserId != null}" class="alert alert-success">
      <b>입력하신 정보와 일치하는 회원정보를 찾았습니다.</b><br><br>
      <p><strong>아이디 :</strong> <span th:text="${FindUserInfoResult.UserId}" class="text-primary"></span></p>
      <p><strong>이름 :</strong> <span th:text="${FindUserInfoResult.UserName}"></span></p>
      <p><strong>주민번호 앞 6자리 :</strong> <span th:text="${FindUserInfoResult.UserRegNum}"></span></p>
      <p><strong>전화번호 :</strong> <span th:text="${FindUserInfoResult.UserPhone}"></span></p>
      <p><strong>주소 :</strong> <span th:text="${FindUserInfoResult.UserAddress1}"></span></p>
      <p><strong>주소 상세 :</strong> <span th:text="${FindUserInfoResult.UserAddress2}"></span></p>
    </div>

    <div th:if="${FindUserInfoResult.UserId == null}" class="alert alert-danger">
      <p>입력하신 정보와 일치하는 회원 정보가 없습니다.</p>
    </div>

    <div th:if="${FindUserInfoResult.UserId != null}">
      <hr>
      <h5>새 비밀번호 설정</h5>
      <form th:action="@{/UserPage/ResetUserPw}" method="POST" onsubmit="return CheckForm(event)">
        <input type="hidden" name="UserId" th:value="${FindUserInfoResult.UserId}">

        <div class="form-group">
          <label>* 아이디</label>
          <input type="text" class="form-control" th:value="${FindUserInfoResult.UserId}" disabled />
        </div>
        <div class="form-group">
          <label>* 비밀번호</label>
          <input type="password" name="UserPw" class="form-control" placeholder="8~20자리 영어+숫자" required />
        </div>
        <div class="form-group">
          <label>* 비밀번호 확인</label>
          <input type="password" name="UserPwc" class="form-control" placeholder="비밀번호 다시 입력" required />
        </div>
        <hr>
        <div class="mt-3">
        <button type="submit" class="btn btn-primary">비밀번호 재설정</button>
        
        <a href="#" th:href="@{/Home}" class="btn btn-secondary" style="float:right;margin-left:5px;">홈으로</a>
      	<a href="#" th:href="@{/UserPage/Login}" class="btn btn-secondary" style="float:right;">로그인</a>
    	</div>
      </form>
    </div>
  </div>
  <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
```


- 아이디 혹은 비밀번호를 잊어버린경우 개인 정보를 입력 후 찾을 수 있습니다.


---

#### 4. 회원 정보 수정 및 회원 탈퇴

![2-s](./images/2-s.png)

```java
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

	 
    public User Fill(User u) throws Exception {
        u.setUserPw(PWSecurity.Hashing(UserPw.getBytes(), u.getUser_Salt()));
        u.setUserName(this.UserName);
        u.setUserRegNum(this.UserRegNum);
        u.setUserGender(this.UserGender);
        u.setUserPhone(this.UserPhone);
        u.setUserAddress1(this.UserAddress1);
        u.setUserAddress2(this.UserAddress2);
        return u;
    }
}
```

```java
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

	//회원정보 수정 기능
	public void UserModify(UserEditDTO uieDTO) throws Exception {
	    User user = this.userRepository.findById(uieDTO.getUserId()).orElseThrow();
	    String inputPwHashed = PWSecurity.Hashing(uieDTO.getUserPw().getBytes(), user.getUser_Salt());
	    if (!inputPwHashed.equals(user.getUserPw())) {
	        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
	    }
	    // 정보 반영 및 저장
	    user = uieDTO.Fill(user);// 이름, 전화번호, 주소 등 업데이트
	    this.userRepository.save(user);
	}

	// 회원 탈퇴 기능
	public boolean DeleteUserId(String UserId, String UserPw) throws Exception {
		User userinfo = this.userRepository.findById(UserId).orElseThrow();
		List<Patient> allpatient = new ArrayList<Patient>();
		allpatient = this.ps.findAll();

		if((userinfo.getUserPw().equals(PWSecurity.Hashing(UserPw.getBytes(), userinfo.getUser_Salt())))) {
			for(Patient patients: allpatient) {
				if(UserId!=null && patients.getP_UserId().equals(UserId)){
					Integer p_id = patients.getP_Id();
					this.pr.deleteById(p_id); // 문진표 삭제
				}
			}
			this.aps.AppointmentDeleteAllByUserId(UserId); // 예약 삭제
			this.userRepository.delete(userinfo); 
			return true;
		}
		return false;
	}
}
```

```java
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/UserPage/EditUserInfo") // 회원정보 수정 화면으로 이동
	public ModelAndView GotoEditUserInfo(HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		String UserId = (String)session.getAttribute("UserId");
		
		if(UserId == null) {
			mav.setViewName("redirect:/Home");
			return mav;
		}
		
		model.addAttribute("UserId",UserId);
		UserReadDTO uiDTO = this.userService.UserInfoRead(UserId);
		mav.addObject("UserInfo",uiDTO);
		mav.setViewName("/UserPage/EditUserInfo");
		return mav;
	}
	
	@PostMapping("/UserPage/EditUserInfo") // 회원 정보 수정 화면에서 정보 입력 후 수정 버튼 누르면 수행
	public String EditUserInfo(UserEditDTO uieDTO, RedirectAttributes rda) throws Exception {
	    try {
	        this.userService.UserModify(uieDTO);
	        return "/UserPage/EditUserInfoResult";
	    } catch (IllegalArgumentException e) { 
	        rda.addFlashAttribute("error", e.getMessage()); // 에러 메시지
	        rda.addFlashAttribute("UserInfo", uieDTO); // 입력값 유지
	        return "redirect:/UserPage/EditUserInfo";
	    }
	}

	@GetMapping("/UserPage/DeleteUserId") // 회원 탈퇴 페이지 이동
	   public String GotoDeleteUserId(HttpSession session, Model model) {
	      String UserId = (String) session.getAttribute("UserId");
	      model.addAttribute("UserId",UserId);
	      
	      if(UserId.equals("admin")) { // 관리자는 회원 탈퇴 불가능하게 컨트롤러에서 우회
	         return "/Home";
	      }
	      else {
	         return "/UserPage/DeleteUserId";
	      }
	}
	
	
	@PostMapping("/UserPage/DeleteUserId") // 회원 탈퇴 진행
	public String DeleteUserId(@RequestParam("UserId") String UserId,
							   @RequestParam("UserPw") String UserPw,
							   HttpSession session, Model model) throws Exception {
		boolean result = this.userService.DeleteUserId(UserId, UserPw);
		if(result == true) {
			session.invalidate();
			return "/UserPage/DeleteUserIdResult";
		}
		else {
			model.addAttribute("UserId",UserId);
			model.addAttribute("Error","비밀번호가 일치하지 않습니다.");
			return "/UserPage/DeleteUserId";
		}
	}
	
	@GetMapping("/UserPage/DeleteUserIdResult") // 회원 탈퇴 결과
	public String GotoDeleteUserIdResult() {
		return "/UserPage/DeleteUserIdResult";
	}
}
```

```html
<div th:replace="fragments/navbar :: navbar"></div>
<header th:insert="/UserPage/Header.html"></header>
  <div class="container mt-5" style="max-width: 400px;">
    <h3>회원 정보 수정</h3>
    <hr>
    <form method="POST" th:object="${UserInfo}" id="UserInfoModifyForm" onsubmit="return CheckForm(event)">
      
      <input type="hidden" name="UserId" th:value="*{UserId}" />
      
      <div class="form-group">
        <label for="UserPw">* 비밀번호</label>
        <input type="password" name="UserPw" id="UserPw" class="form-control" placeholder="8~20자리 영어+숫자" required />
		
      </div>
      
      <div class="form-group">
        <label for="UserPwc">* 비밀번호 확인</label>
        <input type="password" name="UserPwc" id="UserPwc" class="form-control" placeholder="비밀번호 다시 입력" required />
		
		<div th:if="${error}" class="text-danger mt-1" th:text="${error}"></div>
      </div>
<hr>
      <div class="form-group">
        <label for="UserName">* 이름</label>
        <input type="text" name="UserName" id="UserName" th:value="*{UserName}" class="form-control" placeholder="예) 홍길동" required />
      </div>

      <div class="form-group">
        <label for="UserRegNum">* 주민등록번호 앞 6자리</label>
        <input type="text" name="UserRegNum" id="UserRegNum" th:value="*{UserRegNum}" class="form-control" placeholder="예) 921014" required />
      </div>

	  <div class="form-group">
	      <label>* 성별</label><br>
	      <div class="form-check form-check-inline">
	        <input type="radio" name="UserGender" value="1" class="form-check-input" required/>
	        <label class="form-check-label">여성</label>
	      </div>
	      <div class="form-check form-check-inline">
	        <input type="radio" name="UserGender" value="0" class="form-check-input"/>
	        <label class="form-check-label">남성</label>
	      </div>
	    </div>


      <div class="form-group">
        <label for="UserPhone">* 휴대폰 번호</label>
        <input type="text" name="UserPhone" id="UserPhone" th:value="*{UserPhone}" class="form-control" placeholder="예) 01012341234" required />
      </div>

	  <div class="form-group">
	        <label for="UserAddress1">* 주소</label>
	        <div class="input-group">
	            <input type="text" name="UserAddress1" id="UserAddress1" class="form-control" placeholder="주소 검색 버튼을 눌러주세요" required readonly/>
	            <div class="input-group-append">
	                <button type="button" class="btn btn-secondary" onclick="execDaumPostcode()">주소 검색</button>
	            </div>
	        </div>
	    </div>
	  

      <div class="form-group">
        <label for="UserAddress2">* 상세 주소</label>
        <input style="width:100%" type="text" name="UserAddress2" id="UserAddress2" th:value="*{UserAddress2}" class="form-control" placeholder="예) xx빌라 101호" required />
      </div>
      <hr>
	<div class="mt-3 text-right">
      <button type="submit" class="btn btn-primary" style="float:left;">정보 수정</button>
      
      <a href="#" th:href="@{/UserPage/DeleteUserId}" class="btn btn-danger" >회원탈퇴</a>
      <a href="#" th:href="@{/Home}" class="btn btn-secondary" >취소</a>
      
      
    </div>
    </form>
  </div>
```

- 네비게이션바 메뉴의 마이페이지에서 아이디를 제외한 각종 회원 정보를 수정할 수 있으며, 회원 탈퇴 또한 진행 가능합니다.
- 회원 탈퇴시 회원이 등록한 문진표 정보와 예약 정보가 모두 삭제된 후 계정이 삭제됩니다.

---

#### 5. Covid-19 문진표 기능 (환자 등록)

![3-s](./images/3-s.png)

```java
```

```
