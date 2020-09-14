package com.springboot.lock.storage.mysql.model;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName DistributLock
 * @Description
 * @Author 温少
 * @Date 2020/9/9 9:46 上午
 * @Version V1.0
 **/
@Data
public class DistributLock {

    private Long id;

    private String key;

    private String requestId;

    private Long timeout;

    private String unit;

    private Date expirationTime;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

}
