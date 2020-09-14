package com.springboot.lock.storage.mysql.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @ClassName DdlService
 * @Description
 * @Author 温少
 * @Date 2020/9/8 5:54 下午
 * @Version V1.0
 **/
public class LockMysqlDdlDao {

    private static final Logger log = LoggerFactory.getLogger(LockMysqlDdlDao.class);

    @Resource
    private LockMysqlConnection lockMysqlConnection;

    public final static String TABLE_NAME="t_distribut_lock";



    private final static String CREATE_TABLE="create table if not exists t_distribut_lock\n" +
            "(\n" +
            "\tid bigint auto_increment comment '自增主键'\n" +
            "\t\tprimary key,\n" +
            "\t`key` varchar(255) not null comment '资源标识',\n" +
            "\trequest_id varchar(255) not null comment '请求ID',\n" +
            "\ttimeout bigint not null comment '超时时间',\n" +
            "\tunit varchar(50) not null comment '时间单位',\n" +
            "\texpiration_time datetime null comment '过期时间',\n" +
            "\tcreate_time datetime not null comment '创建时间',\n" +
            "\tcreate_user varchar(255) null comment '创建用户',\n" +
            "\tupdate_time datetime not null comment '修改时间',\n" +
            "\tupdate_user varchar(255) null comment '修改用户',\n" +
            "\tconstraint t_distribut_lock_key_uindex\n" +
            "\t\tunique (`key`)\n" +
            ") comment '分布式锁';";

    public void initDataBases(){
        Connection connection = lockMysqlConnection.getConnection();

        if(!tableIsExist(connection)){
            Statement sm = null;
            try {
                sm = connection.createStatement();
                int i = sm.executeUpdate(CREATE_TABLE);
                if(i==0){
                    log.info("create lock table success,{}",TABLE_NAME);
                }else {
                    log.error("create lock table error");
                }
            }catch (Exception e){
                log.error("create lock table error",e);
            }
        }
        lockMysqlConnection.close(connection);

    }

    private boolean tableIsExist(Connection connection){
        //定义一个变量标示
        boolean flag = false ;
        //一个查询该表所有的语句。
        String sql = "SELECT COUNT(1) FROM "+ TABLE_NAME ;
        Statement statement = null;
        ResultSet rs = null;
        //获取连接
        try{
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            flag =  true;
        }catch(Exception e){
            //该表不存在，则 会跳入catch中
            log.error("lock table nonentity,{}",TABLE_NAME);
        }finally{
            //关闭所有连接
            lockMysqlConnection.close(statement,rs);
        }
        return flag;
    }

}
