package com.liahnu.auto_login.client.domain;


public class Config {
    private String server;

    private String ip;
    private Integer strict_bind;
    private Integer double_stack;
    private Integer retry_delay;
    private Integer retry_times;
    private Integer n;
    private Integer type;
    private Integer acid;
    private String os;
    private String name;

    private Integer i;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getRetry_delay() {
        return retry_delay;
    }

    public void setRetry_delay(Integer retry_delay) {
        this.retry_delay = retry_delay;
    }

    public Integer getRetry_times() {
        return retry_times;
    }

    public void setRetry_times(Integer retry_times) {
        this.retry_times = retry_times;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAcid() {
        return acid;
    }

    public void setAcid(Integer acid) {
        this.acid = acid;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStrict_bind(Integer strict_bind) {
        this.strict_bind = strict_bind;
    }

    public void setDouble_stack(Integer double_stack) {
        this.double_stack = double_stack;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public Integer getStrict_bind() {
        return strict_bind;
    }

    public Integer getDouble_stack() {
        return double_stack;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
