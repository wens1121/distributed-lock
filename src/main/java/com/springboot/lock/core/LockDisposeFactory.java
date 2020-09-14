package com.springboot.lock.core;

import com.springboot.lock.entity.LockEntity;

/**
 * @ClassName LockDisposeFactory
 * @Description
 * @Author 温少
 * @Date 2020/9/4 5:36 下午
 * @Version V1.0
 **/
public interface LockDisposeFactory {


    /**
     * @Author 温少
     * @Description  说明：初始化
     * @Date 2020/9/8 5:36 下午
     * @Param  * @param
     * @return void
     **/
    default public void init(){

    }

    /**
     * @Author 温少
     * @Description  说明：获取锁
     * @Date 2020/8/14 10:32 上午
     * @Param  * @param entity
     * @return java.lang.Boolean
     **/
    public Boolean lock(LockEntity entity);

    /**
     * @Author 温少
     * @Description  说明：释放锁
     * @Date 2020/8/14 10:32 上午
     * @Param  * @param entity
     * @return java.lang.Boolean
     **/
    public Boolean unlock(LockEntity entity);



    /**
     * @Author 温少
     * @Description  说明：结束调用
     * @Date 2020/9/10 6:28 下午
     * @Param  * @param
     * @return void
     **/
    default public void destroy(){

    }
}
