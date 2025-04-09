package Hospital.Error.Service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import Hospital.Error.DTO.ErrorLogRequest;
import Hospital.Error.Entity.ErrorLog;
import Hospital.Error.Entity.ErrorLogRepository;

@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;
    @Transactional
    public void logError(ErrorLogRequest errorLogRequest) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setExceptionClass(errorLogRequest.getExceptionClass());
        errorLog.setMessage(errorLogRequest.getMessage());
        errorLog.setStackTrace(errorLogRequest.getStackTrace());
        errorLog.setPackageName(errorLogRequest.getPackageName());
        errorLog.setClassName(errorLogRequest.getClassName());
        errorLog.setFileName(errorLogRequest.getFileName());
        errorLog.setMethodName(errorLogRequest.getMethodName());
        errorLog.setLineNumber(errorLogRequest.getLineNumber());
        
        errorLogRepository.save(errorLog);
    }
    @Transactional
    public void logError(Throwable e) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setExceptionClass(e.getClass().getName());
        errorLog.setMessage(e.getMessage());
        
        if (e.getStackTrace().length > 0) {
            StackTraceElement element = e.getStackTrace()[0];
            errorLog.setPackageName(extractPackageName(element.getClassName()));
            errorLog.setClassName(element.getClassName());
            errorLog.setFileName(element.getFileName());
            errorLog.setMethodName(element.getMethodName());
            errorLog.setLineNumber(element.getLineNumber());
        }
        
        errorLog.setStackTrace(getStackTraceAsString(e));
        errorLogRepository.save(errorLog);
    }

    private String extractPackageName(String fullClassName) {
        int lastDotIndex = fullClassName.lastIndexOf(".");
        return (lastDotIndex != -1) ? fullClassName.substring(0, lastDotIndex) : "Unknown";
    }
    private String getStackTraceAsString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
    // 모든 에러 로그 조회
    public List<ErrorLog> getAllErrors() {
        return errorLogRepository.findAll();
    }
    
}