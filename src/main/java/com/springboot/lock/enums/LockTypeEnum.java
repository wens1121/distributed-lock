package com.springboot.lock.enums;

/**
 * @ClassName StorageEnum
 * @Description
 * @Author 温少
 * @Date 2020/9/4 6:37 下午
 * @Version V1.0
 **/
public enum LockTypeEnum {

    LOCK, /** 只加锁，方法执行前加锁 */
    UNLOCK, /** 只解锁，方法执行后解锁 */
    AUTO_LOCK /** 自动加解锁，在方法执行前加锁，方法执行后解锁 */
    ;

}
