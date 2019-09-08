package org.techtown.schooler.Model;

public class JoinedChannelInfo {

    private String id;
    private String user_id;
    private String user_pw;
    private String isAlloewd;
    private String pushNotify;

    public String get__Id() {
        return id;
    }

    public String getUser__pw() {
        return user_pw;
    }

    public String getUser__id() {
        return user_id;
    }

    public String getIsAlloewd() {
        return isAlloewd;
    }

    public String getPushNotify() {
        return pushNotify;
    }

    public void set__Id(String id) {
        this.id = id;
    }

    public void setUser__id(String user_id) {
        this.user_id = user_id;
    }

    public void setPushNotify(String pushNotify) {
        this.pushNotify = pushNotify;
    }

    public void setUser__pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public void setIsAlloewd(String isAlloewd) {
        this.isAlloewd = isAlloewd;
    }
}
