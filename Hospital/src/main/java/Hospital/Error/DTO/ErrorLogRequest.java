package Hospital.Error.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogRequest {
    private String exceptionClass;
    private String message;
    private String stackTrace;
    private String packageName;
    private String className;
    private String fileName;
    private String methodName;
    private Integer lineNumber;
}