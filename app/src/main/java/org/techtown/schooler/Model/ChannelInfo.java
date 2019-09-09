package org.techtown.schooler.Model;

import android.widget.Switch;

public class ChannelInfo {
    private String name;
    private String explain;
    private String create_user;

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String get_Name() {
        return name;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

}
