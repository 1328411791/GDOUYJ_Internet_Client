package com.liahnu.auto_login.domain;

import java.util.List;

public class Config {
    private String server;
    private Boolean strict_bind;
    private Boolean double_stack;
    private Integer retry_delay;
    private Integer retry_times;
    private Integer n;
    private Integer type;
    private Integer acid;
    private String os;
    private String name;
    private List<User> users;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Boolean getStrict_bind() {
        return strict_bind;
    }

    public void setStrict_bind(Boolean strict_bind) {
        this.strict_bind = strict_bind;
    }

    public Boolean getDouble_stack() {
        return double_stack;
    }

    public void setDouble_stack(Boolean double_stack) {
        this.double_stack = double_stack;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Config{" +
                "server='" + server + '\'' +
                ", strict_bind=" + strict_bind +
                ", double_stack=" + double_stack +
                ", retry_delay=" + retry_delay +
                ", retry_times=" + retry_times +
                ", n=" + n +
                ", type=" + type +
                ", acid=" + acid +
                ", os='" + os + '\'' +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
