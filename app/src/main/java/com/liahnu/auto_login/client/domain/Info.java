package com.liahnu.auto_login.client.domain;

public class Info {

    private String username;
    private String password;
    private String ip;
    private Integer ac_id;
    private String enc_ver;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getAc_id() {
        return ac_id;
    }

    public void setAc_id(Integer ac_id) {
        this.ac_id = ac_id;
    }

    public String getEnc_ver() {
        return enc_ver;
    }

    public void setEnc_ver(String enc_ver) {
        this.enc_ver = enc_ver;
    }
}
