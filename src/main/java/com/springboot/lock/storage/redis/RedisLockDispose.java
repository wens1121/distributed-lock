package com.springboot.lock.storage.redis;

import com.springboot.lock.annotation.LockDisposeType;
import com.springboot.lock.config.LockConfig;
import com.springboot.lock.core.AbstractLockDisposeFactory;
import com.springboot.lock.core.LockDisposeFactory;
import com.springboot.lock.enums.StorageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnClass(RedisTemplate.class)
@Order(30)
@LockDisposeType(name = "redis")
public class RedisLockDispose extends AbstractLockDisposeFactory {

    private static final Logger log = LoggerFactory.getLogger(RedisLockDispose.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private LockConfig lockConfig;

    private static final Long TRUE = 1L;


    /**
     * @Author 温少
     * @Description 说明：初始化
     * @Date 2020/9/8 5:36 下午
     * @Param * @param
     **/
    @Override
    public void init() {
        super.setLockConfig(lockConfig);
    }

    /**
     * @param key
     * @return java.lang.String
     * @Author 温少
     * @Description 说明：获取锁的值（请求ID）
     * @Date 2020/9/7 10:20 上午
     * @Param * @param key
     */
    @Override
    public String get(String key) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(o!=null){
                return (String)o;
            }
        }catch (Exception e){
            log.error("get lock value error ",e);
            throw e;
        }
        return null;
    }

    /**
     * @param lockKey
     * @param requestId 请求ID
     * @param timeout   超时时间
     * @param timeUnit  单位
     * @return void
     * @Author 温少
     * @Description 说明：创建锁
     * @Date 2020/9/7 10:21 上午
     * @Param * @param lockKey 锁标识
     **/
    @Override
    public Boolean create(String lockKey, String requestId, Long timeout, TimeUnit timeUnit) {
        try {
            if(redisTemplate.opsForValue().setIfAbsent(lockKey,requestId)){
                redisTemplate.expire(lockKey,timeout,timeUnit);
                return true;
            }
        }catch (Exception e){
            log.error("lock create error ",e);
        }
        return false;
    }

    /**
     * @param key
     * @param requestId
     * @return void
     * @Author 温少
     * @Description 说明：删除锁
     * @Date 2020/9/7 10:22 上午
     * @Param * @param key
     */
    @Override
    public Boolean del(String key, String requestId) {

        String scriptText = "if redis.call('get',KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call('del',KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
        redisScript.setScriptText(scriptText);
        redisScript.setResultType(Long.class);
        //没有指定序列化方式，默认使用上面配置的
        Long result = (Long) redisTemplate.execute(redisScript, Arrays.asList(key), requestId);
        return TRUE.equals(result);
    }

}
