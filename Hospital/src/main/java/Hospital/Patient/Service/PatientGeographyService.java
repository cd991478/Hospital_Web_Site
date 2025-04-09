package Hospital.Patient.Service;


import org.springframework.http.HttpEntity;

import java.math.BigDecimal;
import java.math.MathContext;

import org.json.JSONObject; // 🔹 반드시 추가해야 함


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PatientGeographyService {
	  private static final String KAKAO_API_KEY 
	  			= "fbdd2e16006bc8e28fa78e3d9d369545"; //카카오 REST API 키
	    private static final String KAKAO_GEOCODE_URL//변환 수행 URL
	    		= "https://dapi.kakao.com/v2/local/search/address.json?query=";
	    public BigDecimal[] getCoordinates(String address) {
	        try {
	        	RestTemplate restTemplate = new RestTemplate();
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
	            headers.set("KA", "HospitalService/1.0 os/Windows lang/Java"); 
	            // 카카오가 요구하는 KA 헤더 추가
	            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"); 
	            // User-Agent 추가
	            headers.set("Accept-Charset", "UTF-8"); // UTF-8 인코딩 설정
	            // JavaScript 키는 클라이언트(SPA, 웹)에서 사용
	            // REST API 키는 서버(Spring Boot, Node.js 등)에서 사용
	            HttpEntity<String> entity = new HttpEntity<>(headers);
	            ResponseEntity<String> response = restTemplate.exchange(
	                KAKAO_GEOCODE_URL + address, HttpMethod.GET, entity, String.class);
	            JSONObject jsonObject = new JSONObject(response.getBody());
	            if (jsonObject.getJSONArray("documents").length() > 0) {
	                JSONObject location = jsonObject.getJSONArray("documents").getJSONObject(0);
	                BigDecimal latitude = location.getBigDecimal("y");
	                BigDecimal longitude = location.getBigDecimal("x");
	                return new BigDecimal[]{latitude, longitude};
	            }
	        } catch (Exception e) {
	            System.err.println("[GEO ERROR] 주소 변환 중 오류 발생: " + e.getMessage());
	        }
	        return null; // 변환 실패
	    }
}
