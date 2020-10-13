package com.springboot.lock.storage.mysql.jdbc;

import com.springboot.lock.storage.mysql.model.DistributLock;
import com.springboot.lock.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName DmlService
 * @Description
 * @Author 温少
 * @Date 2020/9/8 5:54 下午
 * @Version V1.0
 **/
public class LockMysqlDmlDao {

    private static final Logger log = LoggerFactory.getLogger(LockMysqlDmlDao.class);

    @Resource
    private LockMysqlConnection lockMysqlConnection;

    public boolean insert(String lockKey, String requestId, Long timeout, String timeUnit,String expirationTime){
        Connection connection = lockMysqlConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into  t_distribut_lock(`key`, request_id, timeout, unit, expiration_time, create_time, update_time) values(?,?,?,?,?,?,?)";

        try {
            Date date = new Date();
            ps = connection.prepareStatement(sql);
            ps.setString(1,lockKey);
            ps.setString(2,requestId);
            ps.setLong(3,timeout);
            ps.setString(4,timeUnit);
            ps.setString(5,expirationTime);
            ps.setString(6, DateUtils.dateToStr(date,DateUtils.TIME_FORMAT));
            ps.setString(7,DateUtils.dateToStr(date,DateUtils.TIME_FORMAT));

            int result = ps.executeUpdate();
            if(result != 0){
                return true;
            }
        }catch (Exception e){
            log.error("insert {} error",LockMysqlDdlDao.TABLE_NAME,e);
        }finally {
            lockMysqlConnection.close(ps,connection);
        }
        return false;
    }


    /**
     * @Author 温少
     * @Description  说明：通过key查询锁记录
     * @Date 2020/9/9 9:50 上午
     * @Param  * @param key
     * @return com.springboot.lock.storage.mysql.model.DistributLock
     **/
    public DistributLock selectOneByKey(Connection connection , String key){

        Connection conn ;
        if(connection == null){
            conn = lockMysqlConnection.getConnection();
        }else {
            conn = connection;
        }

        String sql = "select id, `key`, request_id, timeout, unit, expiration_time, create_time, create_user, update_time, update_user from t_distribut_lock where `key` = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        DistributLock resule = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,key);
            rs = ps.executeQuery();
            List<DistributLock> list = new ArrayList<>();
            while (rs.next()){
                DistributLock distributLock = this.setDate(rs);
                list.add(distributLock);
            }

            if(list.size()>0){
                resule = list.get(0);
            }
            return resule;
        }catch (Exception e){
            log.error("查询锁记录失败：{}",key,e);
        }finally {
            lockMysqlConnection.close(rs,ps);
            if(connection == null){
                lockMysqlConnection.close(conn);
            }
        }
        return resule;
    }


    /**
     * @Author 温少
     * @Description  说明：查询过期数据
     * @Date 2020/9/9 10:32 上午
     * @Param  * @param connection
     **/
    public List<DistributLock> selectExpirationTimeList(Connection connection ){

        Connection conn ;
        if(connection == null){
            conn = lockMysqlConnection.getConnection();
        }else {
            conn = connection;
        }
        String sql = "select id, `key`, request_id, timeout, unit, expiration_time, create_time, create_user, update_time, update_user from t_distribut_lock where expiration_time < ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DistributLock> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,DateUtils.dateToStr(new Date(),DateUtils.TIME_FORMAT));
            rs = ps.executeQuery();

            while (rs.next()){
                DistributLock distributLock = this.setDate(rs);
                list.add(distributLock);
            }
        }catch (Exception e){
            log.error("查询过期锁记录失败",e);
        }finally {
            lockMysqlConnection.close(rs,ps);
            if(connection == null){
                lockMysqlConnection.close(conn);
            }
        }
        return list;
    }

    /**
     * @Author 温少
     * @Description  说明：查询过期数据
     * @Date 2020/9/9 10:32 上午
     * @Param  * @param connection
     **/
    public Integer delExpirationTime(Connection connection ){

        Connection conn ;
        if(connection == null){
            conn = lockMysqlConnection.getConnection();
        }else {
            conn = connection;
        }
        String sql = "delete from t_distribut_lock where expiration_time < ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,DateUtils.dateToStr(new Date(),DateUtils.TIME_FORMAT));
            int i = ps.executeUpdate();
            return i;
        }catch (Exception e){
            log.error("查询过期锁记录失败",e);
        }finally {
            lockMysqlConnection.close(ps);
            if(connection == null){
                lockMysqlConnection.close(conn);
            }
        }
        return 0;
    }

    /**
     * @Author 温少
     * @Description  说明：根据主键删除
     * @Date 2020/9/9 10:31 上午
     * @Param  * @param connection
     * @param id
     * @return boolean
     **/
    public boolean deltetById(Connection connection ,  Long id){
        Connection conn ;
        if(connection == null){
            conn = lockMysqlConnection.getConnection();
        }else {
            conn = connection;
        }

        String sql = "delete from t_distribut_lock where id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setLong(1,id);
            int i = ps.executeUpdate();
            if(i>0){
                return true;
            }
        }catch (Exception e){
            log.error("del data error：{}",id,e);
        }finally {
            lockMysqlConnection.close(ps);
            if(connection == null){
                lockMysqlConnection.close(conn);
            }
        }
        return false;
    }


    /**
     * @Author 温少
     * @Description  说明：根据key删除
     * @Date 2020/9/9 10:31 上午
     * @Param  * @param connection
     * @param key
     * @return boolean
     **/
    public boolean deltetByKey(Connection connection ,  String key){
        Connection conn ;
        if(connection == null){
            conn = lockMysqlConnection.getConnection();
        }else {
            conn = connection;
        }

        String sql = "delete from t_distribut_lock where `key` = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,key);
            int i = ps.executeUpdate();
            if(i>0){
                return true;
            }
        }catch (Exception e){
            log.error("del data error：{}",key,e);
        }finally {
            lockMysqlConnection.close(ps);
            if(connection == null){
                lockMysqlConnection.close(conn);
            }
        }
        return false;
    }


    public DistributLock setDate(ResultSet rs) throws Exception{
        DistributLock distributLock = new DistributLock();
        distributLock.setId(rs.getLong("id"));
        distributLock.setKey(rs.getString("key"));
        distributLock.setRequestId(rs.getString("request_id"));
        distributLock.setTimeout(rs.getLong("timeout"));
        distributLock.setUnit(rs.getString("unit"));
        distributLock.setExpirationTime(DateUtils.convertDate(rs.getString("expiration_time"),DateUtils.TIME_FORMAT));
        distributLock.setCreateTime(DateUtils.convertDate(rs.getString("create_time"),DateUtils.TIME_FORMAT));
        distributLock.setCreateUser(rs.getString("create_user"));
        distributLock.setUpdateTime(DateUtils.convertDate(rs.getString("update_time"),DateUtils.TIME_FORMAT));
        distributLock.setUpdateUser(rs.getString("update_user"));

        return distributLock;
    }



}
