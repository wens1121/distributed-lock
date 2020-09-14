package com.springboot.lock.exceptions;

/**
 * @ClassName DistributedLockException
 * @Description 分布式锁异常
 * @Author 温少
 * @Date 2020/9/4 5:48 下午
 * @Version V1.0
 **/
public class DistributedLockException extends RuntimeException{

    public DistributedLockException(){
        super();
    }

    public DistributedLockException(String msg){
        super(msg);
    }


}
