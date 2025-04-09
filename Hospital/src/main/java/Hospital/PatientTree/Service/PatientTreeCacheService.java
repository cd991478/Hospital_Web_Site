package Hospital.PatientTree.Service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Entity.PatientRepository;
import Hospital.PatientTree.Entity.PatientBinaryTree;

@Service
public class PatientTreeCacheService {
		@Autowired
	    private CacheManager cacheManager;
		@Autowired
		private PatientRepository patientRepository;
	    @CachePut(value = "patientTreeCache", key = "'patientTree'")
	    
	    public PatientBinaryTree addPatientToCache(Patient newPatient) {
	        PatientBinaryTree tree = getCachedTree();

	        if (tree == null) {
	            tree = new PatientBinaryTree();
	        }

	        boolean inserted = tree.insert(newPatient);
	        if (!inserted) {
	            System.out.println("[CACHE SKIP] 중복 데이터로 인해 삽입되지 않음: " + newPatient.getP_Id());
	            return tree;
	        }
	        Cache cache = cacheManager.getCache("patientTreeCache");
	        if (cache != null) {
	            cache.put("patientTree", tree);
	            System.out.println("[CACHE UPDATE] 캐시가 업데이트됨: " + tree);
	        }
	        return tree;
	    }
	   
	    public PatientBinaryTree getCachedTree() {
	        Cache cache = cacheManager.getCache("patientTreeCache");
	        if (cache != null) {
	            PatientBinaryTree tree = cache.get("patientTree", PatientBinaryTree.class);
	            if (tree != null) {
	                System.out.println("[CACHE HIT] 트리 캐시에서 데이터 가져옴.");
	                return tree;
	            }
	        }
	        System.out.println("[CACHE MISS] 트리가 캐시에 없음.");
	        return new PatientBinaryTree();
	    }

	    @CacheEvict(value = "patientTreeCache", allEntries = true)
	    public void clearCache() {
	        System.out.println("[CACHE CLEAR] 트리 캐시 초기화 완료");
	    }
	    public Patient getPatientFromTreeCache(Integer P_Id) {
	        Instant start = Instant.now(); // 검색 시작 시간

	        PatientBinaryTree tree = this.getCachedTree(); // 캐시에서 트리 가져오기

	        if (tree == null) {
	            System.out.println("[CACHE MISS] 캐시에 트리가 없음.");
	            return fetchPatientFromDB(P_Id, start);
	        }

	        Patient patient = tree.search(P_Id); // 트리에서 P_Id 검색

	        if (patient != null) {
	            Instant end = Instant.now();
	            long searchTime = Duration.between(start, end).toMillis();
	            System.out.println("[CACHE HIT] P_Id: " + P_Id + " 환자 정보 캐시에서 조회 성공. 검색 시간: " + searchTime + "ms");
	            return patient;
	        } else {
	            System.out.println("[CACHE MISS] P_Id: " + P_Id + " 환자가 캐시에 없음. DB에서 조회...");
	            return fetchPatientFromDB(P_Id, start);
	        }
	    }

	   private Patient fetchPatientFromDB(Integer P_Id, Instant start) {
		    Instant dbStart = Instant.now();
		    Patient patient = patientRepository.findById(P_Id).orElse(null);

		    if (patient != null) {
		    	//tcs.addPatientToCache(patient);
		        System.out.println("[DB RESULT] P_Id: " + P_Id + " 데이터 조회 완료.");
		    }

		    Instant dbEnd = Instant.now();
		    long totalSearchTime = Duration.between(start, dbEnd).toMillis();
		    System.out.println("[DB RESULT] P_Id: " + P_Id + " 전체 검색 시간: " + totalSearchTime + "ms");

		    return patient;
		}
}
