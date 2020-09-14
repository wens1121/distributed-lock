package com.springboot.lock.config;



/**
 * @ClassName ZookeeperConfig
 * @Description
 * @Author 温少
 * @Date 2020/9/9 11:45 上午
 * @Version V1.0
 **/
public class ZookeeperConfig {

    /** ip1:port1,ip2:port2 */
    private String ipPorts="localhost:2181";

    /** 数据存储位置 */
    private String namespace = "springboot";

    /** 会话超时时间 */
    private Integer timeout = 5000;

    public String getIpPorts() {
        return ipPorts;
    }

    public void setIpPorts(String ipPorts) {
        this.ipPorts = ipPorts;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
