#                      分布式锁： Distributed-Lock

## 一、简介

​			**Distributed-Lock **为解决分布式系统中资源争夺而生。在当前分布式、微服务架构盛行的年代，多个服务可能会对第一个资源进行抢夺，如果不加以控制，可能导致无法想象有点后果，或者有一些操作不可以同时多次操作。



​			1、数据的存储目前只支持三种：**Redis、Zookeeper、Mysql**，推荐使用Redis。

​			2、支持超时自动释放锁，防止死锁问题

​			3、使用简单方便，侵入式较低，代码只需要一个注解

​			4、锁的资源ID以及请求标识都支持 **Spel表达式**



## 二、使用

### 1、添加依赖

github地址：https://github.com/wenshao1121567443/distributed-lock



下载源码后进行打包：mvn install



引入依赖

```xml
<dependency>
  <groupId>com.distributed-lock</groupId>
  <artifactId>lock-spring-boot-starter</artifactId>
  <version>1.0.1</version>
</dependency>
```



### 2、配置存储依赖

​	指定存储依赖

```yml
spring:
	dispers-lock:
		storage: redis/zookeeper/mysql
```

默认是可以不做配置的，三种方式是有优先级的，并且会根据依赖环境进行自动装配。

优先级排序：
$$
redis>zookeeper>mysql
$$


### 3、使用环境说明

**Redis**

​	```  依赖RedisTemplate 如果springboot中没有配置RedisTemplate，则不可使用```  

**Zookeeper**

​	依赖pom

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.11.0</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.11.0</version>
    <scope>provided</scope>
</dependency>
```

​	配置

```yml
spring:
  dispers-lock:
    storage: zookeeper
    zookeeper-config:
      ipPorts: 127.0.0.1:2181
```



**Mysql**

```依赖DataSource，如果没有dataSource则不会加载，多数据源情况下通过配置可以指定数据源。```





详细配置可以看  **LockConfig** 类



### 3、代码



使用是特别方便的，只需要关注 **@Lock** 注解

```java
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /** 锁的key */
    String key() ;

    /** 锁的请求ID */
    String id() default "'0'";

    /** 超时时间 */
    long timeout() default 1;

    /** 时间单位 */
    TimeUnit unit() default TimeUnit.HOURS;

    /** 锁类型，默认方法结束后释放锁 */
    LockTypeEnum lockType() default LockTypeEnum.AUTO_LOCK;
}
```





```java
/**
 * @Author 温少
 * @Description  说明： 只加锁，方法执行前加锁
 * @Date 2020/9/11 3:57 下午
 * @Param  * @param
 * @return void
 **/
@Lock(key = "'test'",id = "#user.name + '_aaaaa'",lockType = LockTypeEnum.LOCK)
public void test(User user){
    System.out.println("------------>>>>>>>>"+user);
}


/**
 * @Author 温少
 * @Description  说明： 只解锁，方法执行结束进行解锁
 * @Date 2020/9/11 3:57 下午
 * @Param  * @param
 * @return void
 **/
@Lock(key = "test",lockType = LockTypeEnum.UNLOCK)
public void test2(){
    System.out.println("------------>>>>>>>>");
}


/**
 * @Author 温少
 * @Description  说明： 自动加解锁，方法执行前加锁，执行完解锁
 * @Date 2020/9/11 3:57 下午
 * @Param  * @param
 * @return void
 **/
@Lock(key = "'data'",id = "#id" ,lockType = LockTypeEnum.AUTO_LOCK)
public void test3(String id){
    System.out.println("-----------------"+id);
}
```



