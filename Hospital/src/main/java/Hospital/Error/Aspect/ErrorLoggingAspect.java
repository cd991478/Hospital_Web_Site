package Hospital.Error.Aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import Hospital.Error.Service.ErrorLogService;

import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@Component
public class ErrorLoggingAspect {
	
	@Autowired
    private final ErrorLogService errorLogService;

    
    public ErrorLoggingAspect(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    @Pointcut("execution(* hospital..*(..))")
    public void applicationMethods() {}

    @AfterThrowing(pointcut = "applicationMethods()", throwing = "e")
    public void logError(JoinPoint joinPoint, Throwable e) {
        errorLogService.logError(e);
    }
}
