package com.springboot.lock.config;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName MysqlConfig
 * @Description
 * @Author 温少
 * @Date 2020/9/9 11:45 上午
 * @Version V1.0
 **/
public class MysqlConfig {

    private String dataSourceName;

    private Integer regularlyPurgeTime = 1;

    private TimeUnit timeUnit = TimeUnit.MINUTES;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public Integer getRegularlyPurgeTime() {
        return regularlyPurgeTime;
    }

    public void setRegularlyPurgeTime(Integer regularlyPurgeTime) {
        this.regularlyPurgeTime = regularlyPurgeTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
