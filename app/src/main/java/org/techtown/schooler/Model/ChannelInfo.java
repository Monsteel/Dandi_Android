package org.techtown.schooler.Model;

import android.widget.Switch;

public class ChannelInfo {
    private String id;
    private String name;
    private String create_user;
    private String color;
    private String school_id;
    private boolean isPublic;
    private String createdAt;//date//

    public String get_Name() {
        return name;
    }

    public String get_Color() {
        return color;
    }

    public String getCreate_user() {
        return create_user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String get_Id() {
        return id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void set_Id(String id) {
        this.id = id;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public void set_Color(String color) {
        this.color = color;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void set_Public(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
}
