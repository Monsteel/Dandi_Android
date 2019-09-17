package org.techtown.schooler.Model;

import java.util.List;

public class ChannelEvents {

    String id;
    String channel_id;
    String title;
    String start_date;
    String end_date;
    String user_id;
    String user_name;


    public String getId() {
        return id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public String getTitle() {
        return title;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }
}

