package com.springboot.lock.enums;

/**
 * @ClassName StorageEnum
 * @Description
 * @Author 温少
 * @Date 2020/9/4 6:37 下午
 * @Version V1.0
 **/
public enum StorageEnum {

    REDIS("redis"),ZOOKEEPER("zookeeper"),MYSQL("mysql");
    ;

    private String id;
    StorageEnum(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

}
