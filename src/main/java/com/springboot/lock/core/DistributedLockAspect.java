package com.springboot.lock.core;

import com.springboot.lock.annotation.Lock;
import com.springboot.lock.entity.LockEntity;
import com.springboot.lock.enums.LockTypeEnum;
import com.springboot.lock.exceptions.DistributedLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @ClassName LockAspect
 * @Description
 * @Author 温少
 * @Date 2020/8/14 10:10 上午
 * @Version V1.0
 **/
@Aspect
@Slf4j
public class DistributedLockAspect {

    private LockDisposeFactory lockDisposeFactory;

    private ExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();


    public void setLockDisposeFactory(LockDisposeFactory lockDisposeFactory){
        this.lockDisposeFactory = lockDisposeFactory;
    }


    @Pointcut("@annotation(com.springboot.lock.annotation.Lock)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object annotationAround(ProceedingJoinPoint jp) throws Throwable {
        //获取方法
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        String methodName = method.getName();
        EvaluationContext context = this.bindParam(method, jp.getArgs());
        // 获取AspectAnnotation注解
        Lock lock = method.getAnnotation(Lock.class);
        String key = this.getValue(context,lock.key());
        String id = this.getValue(context,lock.id());
        log.debug("{}方法加锁：{},{}",methodName,key,id);
        LockEntity lockEntity = LockEntity.builder()
                .lockKey(key)
                .requestId(id)
                .timeout(lock.timeout())
                .timeUnit(lock.unit())
                .build();
        if(!LockTypeEnum.UNLOCK.equals(lock.lockType())){
            Boolean isLock = lockDisposeFactory.lock(lockEntity);
            log.debug("get lock result:{}",isLock);
            if(!isLock){
//                throw new DistributedLockException(String.format("get Distributed lock error,key:%s ,id:%s,method:%s",key,id,methodName));
                log.warn("get distribut lock error,key:{} ,id:{},method:{}",key,id,methodName);
                return null;
            }
        }

        //执行方法前
        Object returnVal = null;
        try {
            /**执行方法*/
            returnVal = jp.proceed();
        } catch (Exception e) {
            throw e;
        }finally {
            if(!LockTypeEnum.LOCK.equals(lock.lockType())){
                Boolean unlock = lockDisposeFactory.unlock(lockEntity);
                if(!unlock){
                    log.warn("unlock fail,key:{} ,id:{},method:{}",key,id,methodName);
                }
                log.debug("get unlock result:{}",unlock);
            }
            lockDisposeFactory.destroy();
        }
        return returnVal;
    }


    private EvaluationContext bindParam(Method method, Object[] args) {
        //获取方法的参数名
        String[] params = discoverer.getParameterNames(method);
        //将参数名与参数值对应起来
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        return context;
    }

    public String getValue(EvaluationContext context,String spel){
        try {
            Object value = parser.parseExpression(spel).getValue(context);
            return String.valueOf(value);
        }catch (Exception e){
            log.error("SPEL analysis error",e);
            throw new DistributedLockException("SPEL analysis error");
        }

    }

}
