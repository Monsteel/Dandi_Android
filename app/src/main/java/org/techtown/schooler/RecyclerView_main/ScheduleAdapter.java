package org.techtown.schooler.RecyclerView_main;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.ChannelEvents;
import org.techtown.schooler.R;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView channel_name;
        TextView user_name;
        TextView user_id;
        TextView schedule_title;
        TextView start_date;
        TextView end_date;
        ImageView channel_image;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MyViewHolder(View view){
            super(view);

           channel_name = view.findViewById(R.id.channel_name);
           user_name = view.findViewById(R.id.user_name);
           user_id = view.findViewById(R.id.user_id);
           schedule_title = view.findViewById(R.id.schedule_title);
           start_date = view.findViewById(R.id.start_date);
           end_date = view.findViewById(R.id.end_date);
           channel_image = view.findViewById(R.id.channel_image);

           // channel_image 둥글게 만들어줍니다.
            channel_image.setBackground(new ShapeDrawable(new OvalShape()));
            channel_image.setClipToOutline(true);
        }
    }

    private ArrayList<ChannelEvents> channelEventsArrayList;

    public ScheduleAdapter(ArrayList<ChannelEvents> channelEventsArrayList){

        this.channelEventsArrayList = channelEventsArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        //myViewHolder.channel_name.setText(channelEventsArrayList.get(position).get);
        myViewHolder.user_name.setText(channelEventsArrayList.get(position).getUser_name());
        myViewHolder.user_id.setText("(" + channelEventsArrayList.get(position).getUser_id() + ")");
        myViewHolder.schedule_title.setText(channelEventsArrayList.get(position).getTitle());
        myViewHolder.start_date.setText(channelEventsArrayList.get(position).getStart_date());
        myViewHolder.end_date.setText(channelEventsArrayList.get(position).getEnd_date());

    }

    @Override
    public int getItemCount() {

        return channelEventsArrayList.size();
    }
}
