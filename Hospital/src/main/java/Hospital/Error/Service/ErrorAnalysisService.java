package Hospital.Error.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import Hospital.Error.Entity.ErrorLogRepository;

@Service
public class ErrorAnalysisService {

    private final ErrorLogRepository errorLogRepository;

    public ErrorAnalysisService(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    public Map<String, Object> getErrorStatistics() {
        Map<String, Object> stats = new HashMap<>();
        // 총 에러 개수
        long totalErrors = errorLogRepository.count();

        // 가장 많이 발생한 예외 유형
        List<Object[]> topExceptions = errorLogRepository.findTopExceptions();

        // 가장 에러가 많이 발생한 클래스
        List<Object[]> topClasses = errorLogRepository.findTopErrorClasses();

        stats.put("totalErrors", totalErrors);
        stats.put("topExceptions", topExceptions);
        stats.put("topErrorClasses", topClasses);

        return stats;
    }
}