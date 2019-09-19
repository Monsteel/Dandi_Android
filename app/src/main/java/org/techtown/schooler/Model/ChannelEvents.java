package org.techtown.schooler.Model;

import java.util.List;

public class ChannelEvents {

    String user_id;
    String user_name;
    String title;
    String start_date;
    String end_date;

    public ChannelEvents(String user_id, String user_name, String title, String start_date, String end_date){

        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
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

