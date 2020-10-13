package com.springboot.lock.storage.mysql;

import com.springboot.lock.annotation.LockDisposeType;
import com.springboot.lock.config.LockConfig;
import com.springboot.lock.config.MysqlConfig;
import com.springboot.lock.core.AbstractLockDisposeFactory;
import com.springboot.lock.exceptions.DistributedLockException;
import com.springboot.lock.storage.mysql.jdbc.LockMysqlConnection;
import com.springboot.lock.storage.mysql.jdbc.LockMysqlDdlDao;
import com.springboot.lock.storage.mysql.jdbc.LockMysqlDmlDao;
import com.springboot.lock.storage.mysql.model.DistributLock;
import com.springboot.lock.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Order(100)
@ConditionalOnClass(DataSource.class)
@LockDisposeType(name = "mysql")
@Import({LockMysqlDdlDao.class, LockMysqlConnection.class, LockMysqlDmlDao.class})
public class MysqlLockDispose extends AbstractLockDisposeFactory {


    private static final Logger log = LoggerFactory.getLogger(MysqlLockDispose.class);


    @Resource
    private LockConfig lockConfig;

    @Resource
    private LockMysqlConnection lockMysqlConnection;

    @Resource
    private LockMysqlDdlDao lockMysqlDdlDao;

    @Resource
    private LockMysqlDmlDao lockMysqlDmlDao;

    private ScheduledExecutorService executor;



    /**
     * @Author 温少
     * @Description  说明：初始化数据库
     * @Date 2020/9/8 5:37 下午
     * @Param  * @param
     **/
    @Override
    public void init() {
        super.setLockConfig(lockConfig);
        MysqlConfig mysqlConfig = lockConfig.getMysqlConfig();
        lockMysqlConnection.init();
        lockMysqlDdlDao.initDataBases();
        executor = Executors.newScheduledThreadPool(1);
        /** 每隔指定时间定期清除过期锁 */
        executor.scheduleAtFixedRate(()->{
            this.closePastLock();
        },1,mysqlConfig.getRegularlyPurgeTime(),mysqlConfig.getTimeUnit());

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
            DistributLock distributLock = lockMysqlDmlDao.selectOneByKey(null,key);
            if(distributLock!=null){
                return distributLock.getRequestId();
            }
            return null;
        }catch (Exception e){
            log.error("get lock requestId error,",e);
            throw e;
        }
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

            long l = timeUnit.toMillis(timeout);
            Long exTime = new Date().getTime()+ l;

            return lockMysqlDmlDao.insert(lockKey, requestId, timeout, timeUnit.name(), DateUtils.long2String(exTime));
        }catch (Exception e){
            log.error("mysql create lock error",e);
            throw e;
        }
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
        Connection connection = null;
        Boolean isSuccess = false;
        try {
            connection = lockMysqlConnection.getConnection();
            connection.setAutoCommit(false);
            DistributLock distributLock = lockMysqlDmlDao.selectOneByKey(connection, key);

            if(distributLock!= null && distributLock.getRequestId().equals(requestId)){
                isSuccess = lockMysqlDmlDao.deltetByKey(connection, key);
            }

            try {
                connection.commit();
            } catch (SQLException throwables) {
                log.error("del lock commit data error");
            }
            return isSuccess;
        }catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                log.error("del lock rollback data error");
            }
            log.error("del lock error",e);
            throw new DistributedLockException("del lock error");
        }finally {
            lockMysqlConnection.close(connection);
        }
    }


    public void closePastLock(){
        try {
            Integer count = lockMysqlDmlDao.delExpirationTime(null);
            log.info("close expiration time count:{}",count);
        }catch (Exception e){
            log.error("close expiration time error",e);
        }
    }

}
