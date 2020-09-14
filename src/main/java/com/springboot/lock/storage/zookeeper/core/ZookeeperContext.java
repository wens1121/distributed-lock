package com.springboot.lock.storage.zookeeper.core;

import com.springboot.lock.utils.SpringContextUtil;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ZookeeperContext
 * @Description
 * @Author 温少
 * @Date 2020/9/10 10:23 上午
 * @Version V1.0
 **/
public class ZookeeperContext {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperContext.class);

    public static ThreadLocal<CuratorFramework> sessionThreadLocal = ThreadLocal.withInitial(() -> null);


    public static CuratorFramework getSession(){
        CuratorFramework curatorFramework = sessionThreadLocal.get();
        if(curatorFramework!=null){
            return curatorFramework;
        }else {
            ZookeeperFactory bean = SpringContextUtil.getBean(ZookeeperFactory.class);
            CuratorFramework session = bean.getSession();
            sessionThreadLocal.set(session);
            return session;
        }
    }

    public static void remove(){
        try {
            CuratorFramework curatorFramework = sessionThreadLocal.get();
            if(curatorFramework!=null)
                curatorFramework.close();
            sessionThreadLocal.remove();
        }catch (Exception e){
            log.error("zookeeper close session error",e);
        }

    }

}
