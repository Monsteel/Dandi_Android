package org.techtown.schooler.RecyclerView_main;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.ChannelEvents;
import org.techtown.schooler.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView channel_name;
        TextView name;
        TextView id;
        TextView schedule_title;
        //TextView content;
        TextView start_date;
        TextView end_date;

        public MyViewHolder(View view){
            super(view);

           channel_name = view.findViewById(R.id.channel_name);
           name = view.findViewById(R.id.name);
           id = view.findViewById(R.id.id);
           schedule_title = view.findViewById(R.id.schedule_title);
           //content = view.findViewById(R.id.content);
            start_date = view.findViewById(R.id.start_date);
            end_date = view.findViewById(R.id.end_date);
        }
    }

    private ArrayList<ChannelEvents> scheduleInfoArrayList;

    public ScheduleAdapter(ArrayList<ChannelEvents> scheduleInfoArrayList){

        this.scheduleInfoArrayList = scheduleInfoArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

//        myViewHolder.channel_name.setText(scheduleInfoArrayList.get(position).channel_name);
//        myViewHolder.name.setText(scheduleInfoArrayList.get(position).name);
//        myViewHolder.id.setText("(" + scheduleInfoArrayList.get(position).id + ")");
//        myViewHolder.schedule_title.setText(scheduleInfoArrayList.get(position).schedule_title);
//        //myViewHolder.content.setText(scheduleInfoArrayList.get(position).content);
//        myViewHolder.start_date.setText(scheduleInfoArrayList.get(position).start_date);
//        myViewHolder.end_date.setText(scheduleInfoArrayList.get(position).end_date);
    }

    @Override
    public int getItemCount() {

        return scheduleInfoArrayList.size();
    }
}
