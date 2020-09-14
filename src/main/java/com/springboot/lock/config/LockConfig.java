package com.springboot.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LockConfig
 * @Description
 * @Author 温少
 * @Date 2020/9/4 5:29 下午
 * @Version V1.0
 **/
@Component
@ConfigurationProperties(prefix = LockConfig.PREFIX)
public class LockConfig {

    public static final String PREFIX = "spring.dispers-lock";


    /** 默认的超时时间 */
    private Long timeout;

    /** 超时时间的单位 */
    private TimeUnit timeUnit;

    /** 依赖存储方式，redis,zookeeper,mysql */
    private String storage;

    /** mysql配置 */
    private MysqlConfig mysqlConfig = new MysqlConfig();

    private ZookeeperConfig zookeeperConfig = new ZookeeperConfig();

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public MysqlConfig getMysqlConfig() {
        return mysqlConfig;
    }

    public void setMysqlConfig(MysqlConfig mysqlConfig) {
        this.mysqlConfig = mysqlConfig;
    }

    public ZookeeperConfig getZookeeperConfig() {
        return zookeeperConfig;
    }

    public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }
}
