package org.techtown.schooler.RecyclerView_main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.CreateChannelEvents;
import org.techtown.schooler.Model.Events;
import org.techtown.schooler.R;

import java.util.ArrayList;

public class NoScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout layout;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MyViewHolder(View view){
            super(view);

            layout = view.findViewById(R.id.layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(view.getContext(), CreateChannelEvents.class);

                    view.getContext().startActivity(intent);

                    Activity activity = (Activity) view.getContext();

                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });

        }
    }

    private ArrayList<Events> channelEventsArrayList;

    public NoScheduleAdapter(ArrayList<Events> channelEventsArrayList){

        this.channelEventsArrayList = channelEventsArrayList;
    }

    public NoScheduleAdapter(){


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_schedule, parent, false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return channelEventsArrayList.size();
    }

}
