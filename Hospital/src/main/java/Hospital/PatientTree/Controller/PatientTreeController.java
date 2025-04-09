package Hospital.PatientTree.Controller;



import Hospital.Patient.Entity.Patient;
import Hospital.PatientTree.Entity.PatientBinaryTree;
import Hospital.PatientTree.Entity.PatientNode;
import Hospital.PatientTree.Service.PatientTreeCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class PatientTreeController {
	@Autowired
	private PatientTreeCacheService cacheService;
	@Autowired
    private  ObjectMapper objectMapper;

    
	@GetMapping("/api/patient/{pId}")
	public ResponseEntity<PatientNode> getPatient(@PathVariable("pId") Integer P_Id) {
	    Patient patient = cacheService.getPatientFromTreeCache(P_Id);
	    if (patient != null) {
	        PatientNode node = new PatientNode(patient); // ✅ JSON 변환용 래퍼
	        return ResponseEntity.ok(node);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }
	}

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        cacheService.addPatientToCache(patient);
        return ResponseEntity.ok("환자 정보가 추가되었습니다.");
    }

    @GetMapping("/api/patient/tree")
    public ResponseEntity<String> getPatientTree() {
        PatientBinaryTree tree = cacheService.getCachedTree();
        if (tree == null || tree.getRoot() == null) {
            return ResponseEntity.ok("{}"); // 트리가 없을 경우 빈 JSON 반환
        }

        try {
            String jsonTree = objectMapper.writeValueAsString(tree.getRoot()); // 루트 노드부터 변환
            System.out.println("[API RESPONSE] 변환된 트리: " + jsonTree);
            return ResponseEntity.ok(jsonTree);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"JSON 변환 오류\"}");
        }
    }
    @DeleteMapping("/api/patient/cache")
    public ResponseEntity<String> clearCache() {
        cacheService.clearCache();
        return ResponseEntity.ok("캐시가 초기화되었습니다.");
    }
}
