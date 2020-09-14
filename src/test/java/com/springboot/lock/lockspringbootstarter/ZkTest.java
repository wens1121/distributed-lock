package com.springboot.lock.lockspringbootstarter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @ClassName ZkTest
 * @Description
 * @Author 温少
 * @Date 2020/9/9 6:32 下午
 * @Version V1.0
 **/
public class ZkTest {

    private final static String CONNECTSTRING="127.0.0.1:2181";


//    @Test
    public void test1(){
        CuratorFramework session = getSession();

        session.start();
        try {
            String s = session.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath("/distributed-lock/locks/test", "wenshao".getBytes());

            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("success");
        try {
            Thread.sleep(500000L);
            session.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
//        try {
//            Thread.sleep(5000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

//    @Test
    public void test2(){
        CuratorFramework session = getSession();
        session.start();
        try {
            PathChildrenCache cache=new PathChildrenCache(session,"/springboot/ws",true);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

            cache.getListenable().addListener((curatorFramework,pathChildrenCacheEvent)->{
                switch (pathChildrenCacheEvent.getType()){
                    case CHILD_ADDED:
                        System.out.println("增加子节点");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("删除子节点");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("更新子节点");
                        break;
                    default:break;
                }
            });
        }catch (Exception e){
            System.out.println("异常");
        }

        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void getData(){
        try {
            CuratorFramework session = getSession();
            session.start();
            List<String> strings = session.getChildren().forPath("/springboot/ws");



            Stat stat=new Stat();
            byte[] bytes = session.getData().storingStatIn(stat).forPath("/springboot/ws/test");
            String s = new String(bytes);
            System.out.println(s);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static CuratorFramework getSession(){
        //fluent风格
        CuratorFramework session= CuratorFrameworkFactory
                .builder()
                .connectString(CONNECTSTRING)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("springboot")
                .build();

        return session;
    }


}
