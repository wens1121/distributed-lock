package com.springboot.lock.storage.zookeeper;

import com.springboot.lock.annotation.LockDisposeType;
import com.springboot.lock.core.AbstractLockDisposeFactory;
import com.springboot.lock.storage.zookeeper.core.ZookeeperContext;
import com.springboot.lock.storage.zookeeper.core.ZookeeperFactory;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Component
@Order(40)
@ConditionalOnClass(CuratorFramework.class)
@LockDisposeType(name = "zookeeper")
@Import(ZookeeperFactory.class)
public class ZookeeperLockDispose extends AbstractLockDisposeFactory {


    @Resource
    private ZookeeperFactory zookeeperFactory;


    /**
     * @return void
     * @Author 温少
     * @Description 说明：初始化
     * @Date 2020/9/8 5:36 下午
     * @Param * @param
     **/
    @Override
    public void init() {
        zookeeperFactory.createDefaultRoot();
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
        CuratorFramework session = ZookeeperContext.getSession();
        String value = zookeeperFactory.getValue(session,key);
        return value;
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
        CuratorFramework session = ZookeeperContext.getSession();
        return zookeeperFactory.createNode(session, lockKey, requestId);
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
        CuratorFramework session = ZookeeperContext.getSession();
        return zookeeperFactory.delNode(session, key, requestId);
    }

    /**
     * @return void
     * @Author 温少
     * @Description 说明：结束调用
     * @Date 2020/9/10 6:28 下午
     * @Param * @param
     **/
    @Override
    public void destroy() {
        ZookeeperContext.remove();
    }
}
