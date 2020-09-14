package com.springboot.lock.annotation;

import com.springboot.lock.enums.StorageEnum;

import java.lang.annotation.*;

@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockDisposeType {

    /** 实现名称 */
    String name();
}
