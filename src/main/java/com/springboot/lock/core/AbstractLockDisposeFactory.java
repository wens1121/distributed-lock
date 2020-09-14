package com.springboot.lock.core;

import com.springboot.lock.config.LockConfig;
import com.springboot.lock.entity.LockEntity;
import com.springboot.lock.exceptions.DistributedLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockDisposeFactory1
 * @Description
 * @Author 温少
 * @Date 2020/9/7 10:16 上午
 * @Version V1.0
 **/

public abstract class AbstractLockDisposeFactory implements LockDisposeFactory {


    private static final Logger log = LoggerFactory.getLogger(AbstractLockDisposeFactory.class);

    private static ReentrantLock lock = new ReentrantLock();
    private static ReentrantLock unlock = new ReentrantLock();

    private LockConfig lockConfig;


    public void setLockConfig(LockConfig lockConfig){
        this.lockConfig = lockConfig;
    }

    /**
     * @param entity
     * @return java.lang.Boolean
     * @Author 温少
     * @Description 说明：获取锁
     * @Date 2020/8/14 10:32 上午
     * @Param * @param entity
     */
    @Override
    public Boolean lock(LockEntity entity) {
        try {

            if (entity == null || entity.getLockKey() == null || entity.getRequestId() == null) {
                throw new DistributedLockException("lockKey/requestId not null");
            }
            if (entity.getTimeout() == null) {
                if(lockConfig!=null && lockConfig.getTimeout()!=null)
                    entity.setTimeout(lockConfig.getTimeout());
                else
                    entity.setTimeout(1L);
            }
            if (entity.getTimeUnit() == null) {
                if(lockConfig!=null && lockConfig.getTimeUnit()!=null)
                    entity.setTimeUnit(lockConfig.getTimeUnit());
                else
                    entity.setTimeUnit(TimeUnit.HOURS);
            }
            lock.lock();
            boolean locked = false;
            int tryCount = 3;
            /** 可重入锁 */
            String requestId = get(entity.getLockKey());
            if(requestId != null){
                if( entity.getRequestId().equals(requestId)){
                    return true;
                }else {
                    return false;
                }
            }
            while (!locked && tryCount > 0) {
                locked = create(entity.getLockKey(), entity.getRequestId(), entity.getTimeout(), TimeUnit.HOURS);
                tryCount--;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    log.error("线程被中断" + Thread.currentThread().getId(), e);
                }
            }
            return locked;
        }catch (DistributedLockException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error("lock error,{},{}",entity.getLockKey(),entity.getRequestId(),e);
        }finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * @param entity
     * @return java.lang.Boolean
     * @Author 温少
     * @Description 说明：释放锁
     * @Date 2020/8/14 10:32 上午
     * @Param * @param entity
     */
    @Override
    public Boolean unlock(LockEntity entity) {
        try {
            if (entity == null || entity.getLockKey() == null || entity.getRequestId() == null) {
                throw new DistributedLockException("lockKey/requestId not null");
            }
            unlock.lock();
            return del(entity.getLockKey(),entity.getRequestId());
        }catch (DistributedLockException e){
            log.error("unlock error,{},{}",entity.getLockKey(),entity.getRequestId());
        }catch (Exception e){
            log.error("unlock error,",e);
        }finally {
            unlock.unlock();
        }
        return false;
    }

    /**
     * @Author 温少
     * @Description  说明：获取锁的值（请求ID）
     * @Date 2020/9/7 10:20 上午
     * @Param  * @param key
     * @return java.lang.String
     **/
    public abstract String get(String key);

    /**
     * @Author 温少
     * @Description  说明：创建锁
     * @Date 2020/9/7 10:21 上午
     * @Param  * @param lockKey 锁标识
     * @param requestId 请求ID
     * @param timeout  超时时间
     * @param timeUnit 单位
     * @return void
     **/
    public abstract Boolean create(String lockKey, String requestId, Long timeout, TimeUnit timeUnit);

    /**
     * @Author 温少
     * @Description  说明：删除锁
     * @Date 2020/9/7 10:22 上午
     * @Param  * @param key
     * @return void
     **/
    public abstract Boolean del(String key,String requestId);
}
