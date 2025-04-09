package Hospital.Error.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "error_logs")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime timestamp;
    private String exceptionClass;
    private String message;
    @Column(columnDefinition = "TEXT")
    private String stackTrace;
    private String packageName;
    private String className;
    private String fileName;
    private String methodName;
    private Integer lineNumber;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorLog fromException(Throwable e) {
        ErrorLog log = new ErrorLog();
        log.exceptionClass = e.getClass().getName();
        log.message = e.getMessage();

        if (e.getStackTrace().length > 0) {
            StackTraceElement element = e.getStackTrace()[0];
            log.packageName = extractPackageName(element.getClassName());
            log.className = element.getClassName();
            log.fileName = element.getFileName();
            log.methodName = element.getMethodName();
            log.lineNumber = element.getLineNumber();
        }
        log.stackTrace = getStackTraceAsString(e);
        return log;
    }

    private static String extractPackageName(String fullClassName) {
        int lastDotIndex = fullClassName.lastIndexOf(".");
        return (lastDotIndex != -1) ? fullClassName.substring(0, lastDotIndex) : "Unknown";
    }

    private static String getStackTraceAsString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
