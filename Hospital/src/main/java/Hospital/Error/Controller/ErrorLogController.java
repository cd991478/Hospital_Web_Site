package Hospital.Error.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Hospital.Error.Entity.ErrorLog;
import Hospital.Error.Service.ErrorLogService;

@RestController
@RequestMapping("/api/errors")
public class ErrorLogController {

    @Autowired
    private ErrorLogService errorLogService;

    @GetMapping
    public ResponseEntity<List<ErrorLog>> getErrorLogs() {
        List<ErrorLog> logs = errorLogService.getAllErrors();
        return ResponseEntity.ok(logs);
    }
   
}
