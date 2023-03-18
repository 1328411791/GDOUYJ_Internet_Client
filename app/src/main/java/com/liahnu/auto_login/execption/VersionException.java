package com.liahnu.auto_login.execption;

public class VersionException extends Exception{

    private String nowVersion;

    private String newVersion;

    public VersionException(){
        super();
    }

    public VersionException(String nowVersion, String newVersion){
        this.nowVersion = nowVersion;
        this.newVersion = newVersion;
    }

    public String getNowVersion() {
        return nowVersion;
    }

    public void setNowVersion(String nowVersion) {
        this.nowVersion = nowVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
