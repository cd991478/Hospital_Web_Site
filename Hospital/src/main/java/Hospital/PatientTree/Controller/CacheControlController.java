package Hospital.PatientTree.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import Hospital.PatientTree.Service.PatientTreeCacheService;


@Controller
public class CacheControlController {

    private final PatientTreeCacheService treeCacheService;

    public CacheControlController(PatientTreeCacheService treeCacheService) {
        this.treeCacheService = treeCacheService;
    }

    @GetMapping("/api/cache/clear")
    public ResponseEntity<String> clearCache() {
        treeCacheService.clearCache();
        System.out.println("[CACHE CLEAR] 트리 캐시 초기화 완료");
        return ResponseEntity.ok("트리 캐시 초기화 완료");
    }
}

