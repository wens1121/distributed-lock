package com.springboot.lock.storage.zookeeper.core;

import com.springboot.lock.config.LockConfig;
import com.springboot.lock.config.ZookeeperConfig;
import com.springboot.lock.utils.SpringContextUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ZookeeperFactory
 * @Description
 * @Author 温少
 * @Date 2020/9/10 10:21 上午
 * @Version V1.0
 **/
public class ZookeeperFactory {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperFactory.class);


    private final static String DEFAULT_ROOT = "/distributed-lock/locks";


    @Resource
    public LockConfig lockConfig;



    public void createDefaultRoot(){
        CuratorFramework session = null;
        try {
            session = getSession();
            List<String> nodes = session.getChildren().forPath("/");
            if(nodes.contains("distributed-lock")){
                List<String> list = session.getChildren().forPath("/distributed-lock");
                if(list.contains("locks")){
                    return;
                }
            }
            session.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(DEFAULT_ROOT);
        }catch (Exception e){
            log.error("zookeeper create DEFAULT_ROOT error",e);
            ApplicationContext context = SpringContextUtil.getContext();
            SpringApplication.exit(context);
            log.info("server stop success");
        }finally {
//            if(session != null){
//                session.close();
//            }
        }

    }


    /**
     * @Author 温少
     * @Description  说明：获取会话
     * @Date 2020/9/10 2:29 下午
     * @Param  * @param
     * @return org.apache.curator.framework.CuratorFramework
     **/
    public CuratorFramework getSession(){
        ZookeeperConfig zookeeperConfig = lockConfig.getZookeeperConfig();
        CuratorFramework session= CuratorFrameworkFactory
                .builder()
                .connectString(zookeeperConfig.getIpPorts())
                .sessionTimeoutMs(zookeeperConfig.getTimeout())
                /** 重试策略 */
                .retryPolicy(new ExponentialBackoffRetry(2000,3))
                .namespace(zookeeperConfig.getNamespace())
                .build();
        session.start();
        return session;
    }

    public String getValue(CuratorFramework session,String key){
        String node = "/"+key;
        Stat stat=new Stat();
        String url = DEFAULT_ROOT+node;
        try {
            if(nodeIsExist(session,key)){
                byte[] bytes = session.getData().storingStatIn(stat).forPath(url);
                return new String(bytes);
            }
        }catch (Exception e){
            log.error("zookeeper get data error:{}",node,e);
        }
        return null;
    }


    /**
     * @Author 温少
     * @Description  说明：创建节点
     * @Date 2020/9/10 11:47 上午
     * @Param  * @param session
     * @param key
     * @param requestId
     * @return boolean
     **/
    public boolean createNode(CuratorFramework session,String key,String requestId){
        String node = "/"+key;
        String url = DEFAULT_ROOT+node;
        try {
            if (!nodeIsExist(session,key)){
                session.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(url, requestId.getBytes());
                return true;
            }
        }catch (Exception e){
            log.error("zookeeper create node error,node:{}",node);
        }
        return false;
    }

    public boolean delNode(CuratorFramework session,String key,String requestId){
        String node = "/"+key;
        String url = DEFAULT_ROOT+node;
        try {
            if(nodeIsExist(session,key)){
                String value = getValue(session, key);
                if(requestId.equals(value)){
                    session.delete().forPath(url);
                    return true;
                }else {
                    log.info("【{}】not auth del node:{}",requestId,node);
                }
            }
        }catch (Exception e){
            log.error("zookeeper del node node:{}",node);
        }
        return false;
    }


    public boolean nodeIsExist(CuratorFramework session, String node){
        try {
            List<String> nodes = session.getChildren().forPath(DEFAULT_ROOT);
            return nodes.contains(node);
        }catch (Exception e){
            log.error("get node children error url:{}",node,e);
        }
        return false;
    }

}
