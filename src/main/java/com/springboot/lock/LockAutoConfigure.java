package com.springboot.lock;

import com.springboot.lock.annotation.LockDisposeType;
import com.springboot.lock.config.LockConfig;
import com.springboot.lock.core.DistributedLockAspect;
import com.springboot.lock.core.LockDisposeFactory;
import com.springboot.lock.exceptions.DistributedLockException;
import com.springboot.lock.storage.mysql.MysqlLockDispose;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName LockAutoConfigure
 * @Description
 * @Author 温少
 * @Date 2020/9/7 4:53 下午
 * @Version V1.0
 **/
@Configuration
@EnableConfigurationProperties(LockConfig.class)
@ComponentScan
public class LockAutoConfigure implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    @Resource
    private LockConfig lockConfig;

    @Resource
    private List<LockDisposeFactory> lockDisposeFactoryList;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Bean
    public DistributedLockAspect distributedLockAspect() {
        DistributedLockAspect bean = new DistributedLockAspect();
        bean.setLockDisposeFactory(getLockDisposeFactory());
        return bean;
    }


    public LockDisposeFactory getLockDisposeFactory(){
        Map<String, LockDisposeFactory> lockDisposeFactoryMap = lockDisposeFactoryList.stream().collect(Collectors.toMap(x -> {
            Class<? extends LockDisposeFactory> aClass = x.getClass();
            LockDisposeType annotation = aClass.getAnnotation(LockDisposeType.class);
            if (annotation != null) {
                return annotation.name();
            } else {
                return aClass.getSimpleName().replaceAll("LockDispose", "").toLowerCase();
            }
        }, y -> y,(key1,key2)->key1));
        String storage = lockConfig.getStorage();
        LockDisposeFactory lockDisposeFactory;
        if(storage!=null && storage.trim().length()>0){
            lockDisposeFactory = lockDisposeFactoryMap.get(storage);
        }else {
            if(lockDisposeFactoryList!=null && lockDisposeFactoryList.size()>0)
                lockDisposeFactory = lockDisposeFactoryList.get(0);
            else
                lockDisposeFactory = null;
        }

        if(lockDisposeFactory == null){
            throw new DistributedLockException("LockDisposeFactory storage not is null, com.springboot.lock.storage class");
        }
        lockDisposeFactory.init();
        return lockDisposeFactory;
    }
}
