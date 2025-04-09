package Hospital.Error.Controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hospital.Error.Service.ErrorAnalysisService;

@RestController
@RequestMapping("/api/errors")
public class ErrorAnalysisController {

    private final ErrorAnalysisService errorAnalysisService;

    public ErrorAnalysisController(ErrorAnalysisService errorAnalysisService) {
        this.errorAnalysisService = errorAnalysisService;
    }

    @GetMapping("/stats")
    public Map<String, Object> getErrorStatistics() {
        return errorAnalysisService.getErrorStatistics();
    }
}
