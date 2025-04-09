package Hospital.Appointment.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hospital.Appointment.DTO.AppointmentDTO;
import Hospital.Appointment.DTO.AppointmentListResponseDTO;
import Hospital.Appointment.DTO.AppointmentMapper;
import Hospital.Appointment.DTO.AppointmentReadDTO;
import Hospital.Appointment.Entity.Appointment;
import Hospital.Appointment.Entity.AppointmentRepository;
import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.User.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository apr;

    public List<Appointment> findAll(){
		   return this.apr.findAll();
	   }
    @PersistenceContext
    private EntityManager entityManager; // EntityManager 직접 사용
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO aDTO) {
        User user = entityManager.find(User.class, aDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("해당 사용자 없음: " + aDTO.getUserId());
        }
        D_Hospital hospital = entityManager.find(D_Hospital.class, aDTO.getHospitalId());
        if (hospital == null) {
            throw new IllegalArgumentException("해당 병원 없음: " + aDTO.getHospitalId());
        }
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setHospital(hospital);
        appointment.setPatientName(aDTO.getPatientName());
        appointment.setAppointmentTime(aDTO.getAppointmentTime());
        appointment.setCreatedTime(LocalDateTime.now());
        entityManager.persist(appointment); // 엔터티 저장
        entityManager.flush(); // 즉시 DB에 반영하여 ID 생성
        return AppointmentMapper.toDTO(appointment);
    }
    
    public AppointmentReadDTO AppointmentRead(Integer Id) throws NoSuchElementException{
		   Appointment appointment = this.apr.findById(Id).orElseThrow();
		   return AppointmentReadDTO.AppointmentFactory(appointment);
	}
    
    public void AppointmentDelete(Integer Id) {
		this.apr.deleteById(Id);
	}
    
    public void AppointmentDeleteAllByUserId(String UserId) {
    	List<Appointment> AppointmentAll = new ArrayList<Appointment>();
    	AppointmentAll = this.apr.findAll();
    	Integer id;
    	
    	for(Appointment app : AppointmentAll) {
    		if((UserId.equals(app.getUser().getUserId()))) {
    			id = app.getId();
    			this.apr.deleteById(id);
    		}
    	}
    	
    }
    
    public List<AppointmentListResponseDTO> AppointmentListPage(Integer page, Integer maxsize, String UserId) {
	    // 페이지 번호가 null이면 첫 페이지로 기본값 설정
	    if (page == null || page < 0) {
	        page = 0;
	    }
	    // 전체 예약 데이터 조회
	    List<Appointment> allappointment = this.apr.findAll();  // 전체 데이터 조회
	    // UserId에 맞는 예약만 필터링
	    List<Appointment> filteredappointment = allappointment.stream()
	                                                .filter(appointment -> appointment.getUser().getUserId().equals(UserId))  // UserId로 필터링
	                                                .collect(Collectors.toList());
	 // 필터링된 데이터를 날짜를 기준으로 내림차순 정렬
	    filteredappointment.sort((p1, p2) -> p2.getCreatedTime().compareTo(p1.getCreatedTime()));  // 내림차순 정렬
	    // 전체 데이터 개수
	    long totalElements = filteredappointment.size();

	    // 전체 페이지 수 계산
	    int totalPages = (int) Math.ceil((double) totalElements / maxsize);

	    // 페이지네이션: 현재 페이지의 시작 인덱스 계산
	    int startIndex = page * maxsize;
	    int endIndex = Math.min(startIndex + maxsize, filteredappointment.size());

	    // 현재 페이지에 해당하는 데이터만 잘라서 가져오기
	    List<Appointment> pagedappointment = filteredappointment.subList(startIndex, endIndex);

	    // DTO로 변환하여 반환
	    return pagedappointment.stream()
	                         .map(appointment -> new AppointmentListResponseDTO(appointment.getId(), appointment.getHospital().getH_Name(), appointment.getCreatedTime()))
	                         .collect(Collectors.toList());
	}
	
}