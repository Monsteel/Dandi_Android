package org.techtown.schooler.RecyclerView_main;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.schooler.ChannelEvents.ChannelContent;
import org.techtown.schooler.Model.Events;
import org.techtown.schooler.Model.JoinedChannel;
import org.techtown.schooler.R;

import java.io.InputStream;
import java.util.ArrayList;

public class JoinedChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView channel_name;
        ImageView channel_image;
        ImageView isPublic_image;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public MyViewHolder(View view) {
            super(view);

            channel_name = view.findViewById(R.id.channel_name);
            channel_image = view.findViewById(R.id.channel_Image);
            isPublic_image = view.findViewById(R.id.isPublic);

        }
    }

    private ArrayList<JoinedChannel> joinedChannelArrayList;

    public JoinedChannelAdapter(ArrayList<JoinedChannel> joinedChannelArrayList) {

        this.joinedChannelArrayList = joinedChannelArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joined_channel, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        JoinedChannel item = joinedChannelArrayList.get(position);

        myViewHolder.channel_name.setText(joinedChannelArrayList.get(position).getName());

        Glide.with(((MyViewHolder) holder).channel_image.getContext()).load(item.getThumbnail()).into(myViewHolder.channel_image);


        if(item.getIsPublic().equals("0")){

            myViewHolder.isPublic_image.setImageResource(R.drawable.locked);
        } else {

            myViewHolder.isPublic_image.setImageResource(R.drawable.un_locked);
        }
    }

    @Override
    public int getItemCount() {

        return joinedChannelArrayList.size();
    }

}
