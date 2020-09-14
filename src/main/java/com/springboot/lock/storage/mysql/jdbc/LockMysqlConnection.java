package com.springboot.lock.storage.mysql.jdbc;

import com.springboot.lock.config.LockConfig;
import com.springboot.lock.exceptions.DistributedLockException;
import com.springboot.lock.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @ClassName LockMysqlConnection
 * @Description
 * @Author 温少
 * @Date 2020/9/8 5:57 下午
 * @Version V1.0
 **/
public class LockMysqlConnection {

    private static final Logger log = LoggerFactory.getLogger(LockMysqlConnection.class);

    @Resource
    private LockConfig lockConfig;

    private DataSource dataSource;


    public void init(){
        String dataSourceName = lockConfig.getMysqlConfig().getDataSourceName();
        if(dataSourceName!=null && dataSourceName.length()>0){
            this.dataSource =  SpringContextUtil.getBean(dataSourceName, DataSource.class);
        }else {
            this.dataSource = SpringContextUtil.getBean(DataSource.class);
        }
        if(this.dataSource == null){
            throw new DistributedLockException("dataSource not is null,"+dataSourceName);
        }

    }

    /**
     * @Author 温少
     * @Description  说明：获取数据库连接
     * @Date 2020/9/8 6:02 下午
     * @Param  * @param
     * @return java.sql.Connection
     **/
    public Connection getConnection(){
        try {
            return dataSource.getConnection();
        }catch (Exception e){
            log.error("获取数据库连接异常，",e);
            return null;
        }
    }


    public void close(AutoCloseable... s){
        if(s.length>0){
            for (AutoCloseable autoCloseable : s){
                if(autoCloseable!=null){
                    try {
                        autoCloseable.close();
                    } catch (Exception e) {
                        log.error("close error");
                    }
                }
            }
        }
    }

}
