package bv.nba.challenge.audit;

import bv.nba.challenge.entities.ActionsAudit;
import bv.nba.challenge.enums.StatusEnum;
import bv.nba.challenge.repositories.ActionsAuditRepository;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Aspect
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = Exception.class)
@AllArgsConstructor
public class AuditAspect {

    private final ActionsAuditRepository repository;

    /**
     * using this method in order to log controllers' actions.
     */
    @Around("@annotation(ActionAudit)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {

        var actionAudit = new ActionsAudit();
        try {
            var method = extractMethod(joinPoint);
            var auditAnnotation = method.getAnnotation(ActionAudit.class);
            actionAudit.setAction(auditAnnotation.action().value());
            actionAudit.setStatus(StatusEnum.SUCCESS.value());

            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            // for exception change status to failure
            // and re-throw the exception for the service to proceed with its functionality
            actionAudit.setStatus(StatusEnum.FAILURE.value());
            throw e;
        } finally {
            repository.save(actionAudit);
        }

    }

    private Method extractMethod(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod();
    }



}
