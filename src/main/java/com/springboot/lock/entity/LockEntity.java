package com.springboot.lock.entity;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName LockEntity
 * @Description
 * @Author 温少
 * @Date 2020/9/4 5:37 下午
 * @Version V1.0
 **/
@Data
@Builder
public class LockEntity {
    /** 锁key */
    private String lockKey;
    /** 锁的值，请求ID */
    private String requestId;
    /** 过期时间 */
    private Long timeout;
    /** 单位 */
    private TimeUnit timeUnit;
}
