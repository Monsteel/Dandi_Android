package org.techtown.schooler.RecyclerView_main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.Channel;
import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.Model.Events;
import org.techtown.schooler.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

            // schedule_title 타이틀 내용이 만약 글자 수를 초과할 시 흐르게 보여줍니다.
            schedule_title.setSingleLine(true);
            schedule_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            schedule_title.setSelected(true);
        }
    }

    private ArrayList<Events> channelEventsArrayList;

    public ScheduleAdapter(ArrayList<Events> channelEventsArrayList){

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

        myViewHolder.channel_name.setText(channelEventsArrayList.get(position).getChannel().getName());

        if(channelEventsArrayList.get(position).getAuthor().getUser_id() == null){

            myViewHolder.user_id.setText(channelEventsArrayList.get(position).getAuthor().getUser_id());
        } else{
            myViewHolder.user_id.setText("(" + channelEventsArrayList.get(position).getAuthor().getUser_id() + ")");
        }

        myViewHolder.user_name.setText(channelEventsArrayList.get(position).getAuthor().getUser_name());
        myViewHolder.schedule_title.setText(channelEventsArrayList.get(position).getTitle());
        myViewHolder.start_date.setText(channelEventsArrayList.get(position).getStart_date());
        myViewHolder.end_date.setText(channelEventsArrayList.get(position).getEnd_date());

        //loadBitmap(channelEventsArrayList.get(position).getChannel().getThumbnail());

    }

    @Override
    public int getItemCount() {

        return channelEventsArrayList.size();
    }

//

}
