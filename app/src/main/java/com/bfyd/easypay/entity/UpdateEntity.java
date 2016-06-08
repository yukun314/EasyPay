package com.bfyd.easypay.entity;

/**
 * Created by zyk on 2016/4/28.
 */
public class UpdateEntity {

    //版本号
    public int versionCode = -1;

    //版本名
    public String versionName;

    //版本的下载地址
    public String downloadURL;

    //版本简介
    public String summary;

    //版本下载的apk名字
    public String apkName;

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downloadURL='" + downloadURL + '\'' +
                ", summary='" + summary + '\'' +
                ", apkName='" + apkName + '\'' +
                '}';
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }
}
