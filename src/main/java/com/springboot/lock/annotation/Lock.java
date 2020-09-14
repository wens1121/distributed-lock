package com.springboot.lock.annotation;

import com.springboot.lock.enums.LockTypeEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /** 锁的key */
    String key() ;

    /** 锁的请求ID */
    String id() default "'0'";

    /** 超时时间 */
    long timeout() default 1;

    /** 时间单位 */
    TimeUnit unit() default TimeUnit.HOURS;

    /** 锁类型，默认方法结束后释放锁 */
    LockTypeEnum lockType() default LockTypeEnum.AUTO_LOCK;
}
